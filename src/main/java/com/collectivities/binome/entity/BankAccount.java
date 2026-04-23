package com.collectivities.binome.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount implements FinancialAccount {
    private String id;
    private String accountNumber;
    private String accountName;
    private Double amount;
    private String type = "BANK_TRANSFER";

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Number getAmount() {
        return amount;
    }
}
