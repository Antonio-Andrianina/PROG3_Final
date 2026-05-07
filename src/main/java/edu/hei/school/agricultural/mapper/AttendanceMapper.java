package edu.hei.school.agricultural.mapper;

import edu.hei.school.agricultural.entity.ActivityMemberAttendance;
import edu.hei.school.agricultural.entity.AttendanceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class AttendanceMapper {
    private final MemberMapper memberMapper;

    public ActivityMemberAttendance mapFromResultSet(ResultSet rs) throws SQLException {
        return ActivityMemberAttendance.builder()
                .id(rs.getString("attendance_id"))
                .member(memberMapper.mapFromResultSet(rs))
                .attendanceStatus(rs.getString("attendance_status") == null
                        ? AttendanceStatus.UNDEFINED
                        : AttendanceStatus.valueOf(rs.getString("attendance_status")))
                .build();
    }
}