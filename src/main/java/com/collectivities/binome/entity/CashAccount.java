package com.collectivities.binome.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashAccount implements FinancialAccount {
    private String id;
    private Double amount;
    private String type = "CASH";

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Number getAmount() {
        return amount;
    }
}