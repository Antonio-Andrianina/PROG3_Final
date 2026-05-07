package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.CreateActivityMemberAttendance;
import edu.hei.school.agricultural.entity.ActivityAttendance;
import edu.hei.school.agricultural.entity.AttendanceStatus;
import edu.hei.school.agricultural.repository.MemberRepository;
import edu.hei.school.agricultural.entity.Member;
import edu.hei.school.agricultural.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ActivityAttendanceDtoMapper {
    
    private final MemberRepository memberRepository;
    
    public ActivityAttendanceDtoMapper(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    public ActivityAttendance mapToEntity(CreateActivityMemberAttendance createAttendance, String activityId, String recordedBy) {
        LocalDateTime now = LocalDateTime.now();
        
        // Find member
        Member member = memberRepository.findById(createAttendance.memberIdentifier)
                .orElseThrow(() -> new NotFoundException(
                        "Member.id=" + createAttendance.memberIdentifier + " not found"));
        
        // Create entity using direct field access
        ActivityAttendance attendance = new ActivityAttendance();
        attendance.id = UUID.randomUUID().toString();
        attendance.activityId = activityId;
        attendance.memberId = createAttendance.memberIdentifier;
        attendance.status = createAttendance.attendanceStatus == null 
                ? AttendanceStatus.UNDEFINED 
                : AttendanceStatus.valueOf(createAttendance.attendanceStatus.name());
        attendance.recordedAt = now;
        attendance.recordedBy = recordedBy;
        
        return attendance;
    }
}
