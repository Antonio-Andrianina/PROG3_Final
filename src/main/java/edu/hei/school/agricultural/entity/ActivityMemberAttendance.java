package edu.hei.school.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityMemberAttendance {
    private String id;
    private Member member;
    private AttendanceStatus attendanceStatus;
    private CollectivityActivity activity;
    
    public enum AttendanceStatus {
        MISSING,
        ATTENDED,
        UNDEFINED
    }
}