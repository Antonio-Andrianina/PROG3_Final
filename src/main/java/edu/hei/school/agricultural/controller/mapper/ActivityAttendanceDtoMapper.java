package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.ActivityAttendance;
import edu.hei.school.agricultural.controller.dto.CreateActivityAttendance;
import edu.hei.school.agricultural.entity.ActivityAttendance;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ActivityAttendanceDtoMapper {
    
    public ActivityAttendance mapToDto(edu.hei.school.agricultural.entity.ActivityAttendance attendance) {
        return new ActivityAttendance(
            attendance.getId(),
            attendance.getActivityId(),
            attendance.getMemberId(),
            edu.hei.school.agricultural.controller.dto.ActivityAttendance.AttendanceStatus.valueOf(
                attendance.getStatus().name()
            ),
            attendance.getRecordedAt(),
            attendance.getRecordedBy()
        );
    }
    
    public List<edu.hei.school.agricultural.entity.ActivityAttendance> mapToEntity(CreateActivityAttendance createAttendance, 
                                                   String activityId, String recordedBy) {
        LocalDateTime now = LocalDateTime.now();
        
        // Map present members
        List<edu.hei.school.agricultural.entity.ActivityAttendance> presentAttendances = createAttendance.getPresentMemberIds().stream()
                .map(memberId -> {
                    edu.hei.school.agricultural.entity.ActivityAttendance attendance = new edu.hei.school.agricultural.entity.ActivityAttendance();
                    attendance.setId(UUID.randomUUID().toString());
                    attendance.setActivityId(activityId);
                    attendance.setMemberId(memberId);
                    attendance.setStatus(edu.hei.school.agricultural.entity.ActivityAttendance.AttendanceStatus.PRESENT);
                    attendance.setRecordedAt(now);
                    attendance.setRecordedBy(recordedBy);
                    return attendance;
                })
                .toList();
        
        // Map absent members
        List<edu.hei.school.agricultural.entity.ActivityAttendance> absentAttendances = createAttendance.getAbsentMemberIds().stream()
                .map(memberId -> {
                    edu.hei.school.agricultural.entity.ActivityAttendance attendance = new edu.hei.school.agricultural.entity.ActivityAttendance();
                    attendance.setId(UUID.randomUUID().toString());
                    attendance.setActivityId(activityId);
                    attendance.setMemberId(memberId);
                    attendance.setStatus(edu.hei.school.agricultural.entity.ActivityAttendance.AttendanceStatus.ABSENT);
                    attendance.setRecordedAt(now);
                    attendance.setRecordedBy(recordedBy);
                    return attendance;
                })
                .toList();
        
        // Combine both lists
        return List.of(presentAttendances, absentAttendances).stream()
                .flatMap(List::stream)
                .toList();
    }
}
