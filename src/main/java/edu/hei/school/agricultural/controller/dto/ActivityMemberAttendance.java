package edu.hei.school.agricultural.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ActivityMemberAttendance {
    private String id;
    private MemberDescription memberDescription;
    private AttendanceStatus attendanceStatus;
    
    public enum AttendanceStatus {
        MISSING,
        ATTENDED,
        UNDEFINED
    }
}
