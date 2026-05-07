package edu.hei.school.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount extends FinancialAccount {
    private String holderName;
    private Bank bank;
    private Integer bankCode;
    private Integer bankBranchCode;
    private Integer bankAccountNumber;
    private Integer bankAccountKey;

    public BankAccount(String id, String holderName, Bank bank, Integer bankCode, 
                      Integer bankBranchCode, Integer bankAccountNumber, 
                      Integer bankAccountKey, BigDecimal amount) {
        super();
        this.setId(id);
        this.setAmount(amount);
        this.holderName = holderName;
        this.bank = bank;
        this.bankCode = bankCode;
        this.bankBranchCode = bankBranchCode;
        this.bankAccountNumber = bankAccountNumber;
        this.bankAccountKey = bankAccountKey;
    }
}
