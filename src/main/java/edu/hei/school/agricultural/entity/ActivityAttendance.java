package edu.hei.school.agricultural.entity;

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
@Getter
@Setter
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
