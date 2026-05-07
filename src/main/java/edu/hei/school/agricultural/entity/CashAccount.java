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
public class CashAccount extends FinancialAccount {
    public CashAccount(String id, BigDecimal amount) {
        super();
        this.setId(id);
        this.setAmount(amount);
    }
}
