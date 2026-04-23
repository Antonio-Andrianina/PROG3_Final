package com.collectivities.binome.entity;

import lombok.Data;

@Data
public class CashAccount implements FinancialAccount {
    private String id;
    private Integer amount;
}