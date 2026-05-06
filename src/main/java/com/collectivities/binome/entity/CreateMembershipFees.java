package com.collectivities.binome.entity;

import com.collectivities.binome.controller.Frequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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