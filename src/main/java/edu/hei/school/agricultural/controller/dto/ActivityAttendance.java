package edu.hei.school.agricultural.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ActivityAttendance {
    private String id;
    private String activityId;
    private String memberId;
    private AttendanceStatus status;
    private LocalDateTime recordedAt;
    private String recordedBy;
    
    public enum AttendanceStatus {
        PRESENT,
        ABSENT
    }
}
