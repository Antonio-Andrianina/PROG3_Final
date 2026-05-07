package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.ActivityAttendance as DtoActivityAttendance;
import edu.hei.school.agricultural.controller.dto.CreateActivityAttendance;
import edu.hei.school.agricultural.entity.ActivityAttendance as EntityActivityAttendance;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ActivityAttendanceDtoMapper {
    
    public DtoActivityAttendance mapToDto(EntityActivityAttendance attendance) {
        return new DtoActivityAttendance(
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
    
    public List<EntityActivityAttendance> mapToEntity(CreateActivityAttendance createAttendance, 
                                                   String activityId, String recordedBy) {
        LocalDateTime now = LocalDateTime.now();
        
        // Map present members
        List<EntityActivityAttendance> presentAttendances = createAttendance.getPresentMemberIds().stream()
                .map(memberId -> {
                    EntityActivityAttendance attendance = new EntityActivityAttendance();
                    attendance.setId(UUID.randomUUID().toString());
                    attendance.setActivityId(activityId);
                    attendance.setMemberId(memberId);
                    attendance.setStatus(EntityActivityAttendance.AttendanceStatus.PRESENT);
                    attendance.setRecordedAt(now);
                    attendance.setRecordedBy(recordedBy);
                    return attendance;
                })
                .toList();
        
        // Map absent members
        List<EntityActivityAttendance> absentAttendances = createAttendance.getAbsentMemberIds().stream()
                .map(memberId -> {
                    EntityActivityAttendance attendance = new EntityActivityAttendance();
                    attendance.setId(UUID.randomUUID().toString());
                    attendance.setActivityId(activityId);
                    attendance.setMemberId(memberId);
                    attendance.setStatus(EntityActivityAttendance.AttendanceStatus.ABSENT);
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
