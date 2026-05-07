package edu.hei.school.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectivityTransaction {
    private String id;
    private String collectivityId;
    private String memberDebitedId;
    private LocalDate creationDate;
    private Double amount;
    private String paymentMode;
    private String accountCreditedId;
}