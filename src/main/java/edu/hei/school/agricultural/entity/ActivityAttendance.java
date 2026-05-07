package edu.hei.school.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ActivityAttendance {
    public String id;
    public String activityId;
    public String memberId;
    public AttendanceStatus status;
    public LocalDateTime recordedAt;
    public String recordedBy;
}
