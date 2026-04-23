package com.collectivities.binome.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MembershipFees extends CreateMembershipFees {
    private String id;
    private ActivityStatus status;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public MembershipFees(String id, ActivityStatus status) {
        this.id = id;
        this.status = status;
    }

    public boolean isActive() {
        return status == ActivityStatus.ACTIVE;
    }

    public boolean isInactive() {
        return status == ActivityStatus.INACTIVE;
    }

    public void activate() {
        this.status = ActivityStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = ActivityStatus.INACTIVE;
    }
}