package com.collectivities.binome.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileBankingAccount implements FinancialAccount {
    private String id;
    private String phoneNumber;
    private String provider;
    private Double amount;
    private String type = "MOBILE_BANKING";

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Number getAmount() {
        return amount;
    }
}
