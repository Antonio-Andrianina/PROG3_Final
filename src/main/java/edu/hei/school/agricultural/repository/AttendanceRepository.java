package edu.hei.school.agricultural.repository;



import edu.hei.school.agricultural.entity.ActivityMemberAttendance;
import edu.hei.school.agricultural.entity.AttendanceStatus;
import edu.hei.school.agricultural.mapper.AttendanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AttendanceRepository {
    private final Connection connection;
    private final AttendanceMapper attendanceMapper;

    public List<ActivityMemberAttendance> saveAll(List<ActivityMemberAttendance> attendances) {
        List<ActivityMemberAttendance> saved = new ArrayList<>();
        String sql = """
                insert into "activity_member_attendance" (id, activity_id, member_id, attendance_status)
                values (?, ?, ?, ?::attendance_status)
                on conflict (id) do nothing
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (ActivityMemberAttendance att : attendances) {
                ps.setString(1, att.getId());
                ps.setString(2, att.getActivity().getId());
                ps.setString(3, att.getMember().getId());
                ps.setString(4, att.getAttendanceStatus() == null
                        ? AttendanceStatus.UNDEFINED.name()
                        : att.getAttendanceStatus().name());
                ps.addBatch();
            }
            ps.executeBatch();
            for (ActivityMemberAttendance att : attendances) {
                findById(att.getId()).ifPresent(saved::add);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return saved;
    }

    public Optional<ActivityMemberAttendance> findById(String id) {
        String sql = """
                select ama.id as attendance_id, ama.attendance_status,
                       m.id, m.first_name, m.last_name, m.birth_date, m.gender,
                       m.phone_number, m.email, m.address, m.profession,
                       m.occupation, m.registration_fee_paid, m.membership_dues_paid
                from "activity_member_attendance" ama
                join "member" m on m.id = ama.member_id
                where ama.id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(attendanceMapper.mapFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<ActivityMemberAttendance> findAllByActivityId(String activityId) {
        List<ActivityMemberAttendance> list = new ArrayList<>();
        String sql = """
                select ama.id as attendance_id, ama.attendance_status,
                       m.id, m.first_name, m.last_name, m.birth_date, m.gender,
                       m.phone_number, m.email, m.address, m.profession,
                       m.occupation, m.registration_fee_paid, m.membership_dues_paid
                from "activity_member_attendance" ama
                join "member" m on m.id = ama.member_id
                where ama.activity_id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, activityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(attendanceMapper.mapFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public boolean isAlreadyConfirmed(String activityId, String memberId) {
        String sql = """
                select id from "activity_member_attendance"
                where activity_id = ? and member_id = ?
                  and attendance_status in ('ATTENDED', 'MISSING')
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, activityId);
            ps.setString(2, memberId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Double> getAssiduityPercentageByMember(String collectivityId,
                                                               LocalDate from,
                                                               LocalDate to) {
        Map<String, Double> result = new HashMap<>();
        String sql = """
                select ama.member_id,
                       count(case when ama.attendance_status = 'ATTENDED' then 1 end) as attended,
                       count(*) as total
                from activity_member_attendance ama
                join collectivity_activity ca on ca.id = ama.activity_id
                where ca.collectivity_id = ?
                  and (ca.executive_date between ? and ?
                       or ca.recurrence_week_ordinal is not null)
                group by ama.member_id
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, java.sql.Date.valueOf(from));
            ps.setDate(3, java.sql.Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int attended = rs.getInt("attended");
                int total = rs.getInt("total");
                double pct = total == 0 ? 0.0 : (attended * 100.0) / total;
                result.put(rs.getString("member_id"), pct);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Map<String, Double> getOverallAssiduityPercentageByCollectivity(LocalDate from,
                                                                             LocalDate to) {
        Map<String, Double> result = new HashMap<>();
        String sql = """
                select ca.collectivity_id,
                       count(case when ama.attendance_status = 'ATTENDED' then 1 end) as attended,
                       count(*) as total
                from activity_member_attendance ama
                join collectivity_activity ca on ca.id = ama.activity_id
                where (ca.executive_date between ? and ?
                       or ca.recurrence_week_ordinal is not null)
                group by ca.collectivity_id
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int attended = rs.getInt("attended");
                int total = rs.getInt("total");
                double pct = total == 0 ? 0.0 : (attended * 100.0) / total;
                result.put(rs.getString("collectivity_id"), pct);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}