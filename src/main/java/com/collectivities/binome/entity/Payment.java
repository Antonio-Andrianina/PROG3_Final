package com.collectivities.binome.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private UUID id;
    private UUID memberId;
    private UUID membershipFeesId;
    private Double amount;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;
    private String reference;
    private ActivityStatus status;
    private LocalDate createdAt;
}