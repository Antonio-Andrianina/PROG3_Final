package com.collectivities.binome.controller;


import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMembershipFees {
    private LocalDate eligibleForm;
    private Frequency frequency;
    private Double amount;
    private String label;
}