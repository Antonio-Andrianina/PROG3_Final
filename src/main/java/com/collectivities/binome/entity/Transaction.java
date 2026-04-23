package com.collectivities.binome.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private UUID id;
    private UUID collectivityId;
    private UUID memberId;
    private UUID membershipFeesId;
    private String transactionType;
    private Double amount;
    private LocalDateTime transactionDate;
    private String description;
    private LocalDateTime createdAt;
    private String memberFirstName;
    private String memberLastName;
}