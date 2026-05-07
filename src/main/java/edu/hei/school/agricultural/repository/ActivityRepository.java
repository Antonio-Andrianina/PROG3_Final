package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.entity.CollectivityActivity;
import edu.hei.school.agricultural.entity.MemberOccupation;
import edu.hei.school.agricultural.mapper.ActivityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;

@Repository
@RequiredArgsConstructor
public class ActivityRepository {
    private final Connection connection;
    private final ActivityMapper activityMapper;

    public List<CollectivityActivity> saveAll(List<CollectivityActivity> activities) {
        List<CollectivityActivity> saved = new ArrayList<>();
        String sql = """
                insert into "collectivity_activity"
                    (id, label, activity_type, collectivity_id,
                     recurrence_week_ordinal, recurrence_day_of_week, executive_date)
                values (?, ?, ?::activity_type, ?, ?, ?, ?)
                on conflict (id) do nothing
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (CollectivityActivity activity : activities) {
                ps.setString(1, activity.getId());
                ps.setString(2, activity.getLabel());
                ps.setString(3, activity.getActivityType() == null
                        ? null : activity.getActivityType().name());
                ps.setString(4, activity.getCollectivity().getId());

                if (activity.getRecurrenceRule() != null) {
                    ps.setInt(5, activity.getRecurrenceRule().getWeekOrdinal());
                    ps.setString(6, activity.getRecurrenceRule().getDayOfWeek());
                } else {
                    ps.setNull(5, Types.INTEGER);
                    ps.setNull(6, Types.VARCHAR);
                }

                if (activity.getExecutiveDate() != null) {
                    ps.setDate(7, java.sql.Date.valueOf(activity.getExecutiveDate()));
                } else {
                    ps.setNull(7, Types.DATE);
                }
                ps.addBatch();
            }
            ps.executeBatch();

            for (CollectivityActivity activity : activities) {
                saveOccupationsConcerned(activity);
                saved.add(findById(activity.getId()).orElseThrow());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return saved;
    }

    private void saveOccupationsConcerned(CollectivityActivity activity) throws SQLException {
        if (activity.getMemberOccupationConcerned() == null) return;
        String sql = """
                insert into "activity_occupation_concerned" (id, activity_id, member_occupation)
                values (?, ?, ?)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (MemberOccupation occ : activity.getMemberOccupationConcerned()) {
                ps.setString(1, randomUUID().toString());
                ps.setString(2, activity.getId());
                ps.setString(3, occ.name());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public Optional<CollectivityActivity> findById(String id) {
        String sql = """
                select id, label, activity_type, collectivity_id,
                       recurrence_week_ordinal, recurrence_day_of_week, executive_date
                from "collectivity_activity"
                where id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CollectivityActivity activity = activityMapper.mapFromResultSet(rs);
                activity.setMemberOccupationConcerned(findOccupationsByActivityId(id));
                return Optional.of(activity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<CollectivityActivity> findAllByCollectivityId(String collectivityId) {
        List<CollectivityActivity> activities = new ArrayList<>();
        String sql = """
                select id, label, activity_type, collectivity_id,
                       recurrence_week_ordinal, recurrence_day_of_week, executive_date
                from "collectivity_activity"
                where collectivity_id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CollectivityActivity activity = activityMapper.mapFromResultSet(rs);
                activity.setMemberOccupationConcerned(
                        findOccupationsByActivityId(activity.getId()));
                activities.add(activity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return activities;
    }

    private List<MemberOccupation> findOccupationsByActivityId(String activityId) {
        List<MemberOccupation> list = new ArrayList<>();
        String sql = """
                select member_occupation from "activity_occupation_concerned"
                where activity_id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, activityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(MemberOccupation.valueOf(rs.getString("member_occupation")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}