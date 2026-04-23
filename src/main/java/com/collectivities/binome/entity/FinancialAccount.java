package com.collectivities.binome.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CashAccount.class, name = "CASH"),
    @JsonSubTypes.Type(value = MobileBankingAccount.class, name = "MOBILE_BANKING"),
    @JsonSubTypes.Type(value = BankAccount.class, name = "BANK_TRANSFER")
})
public interface FinancialAccount {
    String getId();
    Number getAmount();
}
