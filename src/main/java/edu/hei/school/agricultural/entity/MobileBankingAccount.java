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
public class MobileBankingAccount extends FinancialAccount {
    private String holderName;
    private MobileBankingService mobileBankingService;
    private Integer mobileNumber;

    public MobileBankingAccount(String id, String holderName, MobileBankingService mobileBankingService, 
                               Integer mobileNumber, BigDecimal amount) {
        super();
        this.setId(id);
        this.setAmount(amount);
        this.holderName = holderName;
        this.mobileBankingService = mobileBankingService;
        this.mobileNumber = mobileNumber;
    }
}
