package com.collectivities.binome.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FederationMandate {
    private Long id;
    private Member member;
    private FederationPosition position;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;

    public boolean isCurrent() {
        LocalDate now = LocalDate.now();
        return startDate != null && endDate != null &&
                !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    public int getRemainingMonths() {
        if (!isCurrent()) return 0;
        LocalDate now = LocalDate.now();
        return (int) java.time.temporal.ChronoUnit.MONTHS.between(now, endDate);
    }
}
