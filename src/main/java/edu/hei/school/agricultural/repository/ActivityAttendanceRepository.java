package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.entity.ActivityAttendance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActivityAttendanceRepository {
    private final Connection connection;

    public List<ActivityAttendance> saveAll(List<ActivityAttendance> attendances) {
        List<ActivityAttendance> savedAttendances = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                insert into activity_attendance (id, activity_id, member_id, status, recorded_at, recorded_by)
                values (?, ?, ?, ?, ?, ?, ?)
                on conflict (activity_id, member_id) do nothing
                """)) {
            
            for (ActivityAttendance attendance : attendances) {
                preparedStatement.setString(1, attendance.getId());
                preparedStatement.setString(2, attendance.getActivityId());
                preparedStatement.setString(3, attendance.getMemberId());
                preparedStatement.setString(4, attendance.getStatus().name());
                preparedStatement.setTimestamp(5, java.sql.Timestamp.valueOf(attendance.getRecordedAt()));
                preparedStatement.setString(6, attendance.getRecordedBy());
                preparedStatement.addBatch();
            }
            
            preparedStatement.executeBatch();
            
            // Return saved attendances
            for (ActivityAttendance attendance : attendances) {
                savedAttendances.add(findById(attendance.getId()).orElseThrow());
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return savedAttendances;
    }

    public Optional<ActivityAttendance> findById(String id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                select id, activity_id, member_id, status, recorded_at, recorded_by
                from activity_attendance
                where id = ?
                """)) {
            
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapFromResultSet(resultSet));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return Optional.empty();
    }

    public List<ActivityAttendance> findByActivityId(String activityId) {
        List<ActivityAttendance> attendances = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                select id, activity_id, member_id, status, recorded_at, recorded_by
                from activity_attendance
                where activity_id = ?
                order by recorded_at desc
                """)) {
            
            preparedStatement.setString(1, activityId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                attendances.add(mapFromResultSet(resultSet));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return attendances;
    }

    public List<ActivityAttendance> findByActivityIdAndStatus(String activityId, ActivityAttendance.AttendanceStatus status) {
        List<ActivityAttendance> attendances = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                select id, activity_id, member_id, status, recorded_at, recorded_by
                from activity_attendance
                where activity_id = ? and status = ?
                order by recorded_at desc
                """)) {
            
            preparedStatement.setString(1, activityId);
            preparedStatement.setString(2, status.name());
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                attendances.add(mapFromResultSet(resultSet));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return attendances;
    }

    public boolean existsByActivityIdAndMemberId(String activityId, String memberId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                select count(*)
                from activity_attendance
                where activity_id = ? and member_id = ?
                """)) {
            
            preparedStatement.setString(1, activityId);
            preparedStatement.setString(2, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return false;
    }

    private ActivityAttendance mapFromResultSet(ResultSet resultSet) throws SQLException {
        ActivityAttendance attendance = new ActivityAttendance();
        attendance.setId(resultSet.getString("id"));
        attendance.setActivityId(resultSet.getString("activity_id"));
        attendance.setMemberId(resultSet.getString("member_id"));
        attendance.setStatus(ActivityAttendance.AttendanceStatus.valueOf(resultSet.getString("status")));
        attendance.setRecordedAt(resultSet.getTimestamp("recorded_at").toLocalDateTime());
        attendance.setRecordedBy(resultSet.getString("recorded_by"));
        return attendance;
    }
}
