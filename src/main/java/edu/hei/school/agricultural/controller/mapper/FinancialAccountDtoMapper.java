package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.FinancialAccount;
import edu.hei.school.agricultural.entity.BankAccount;
import edu.hei.school.agricultural.entity.CashAccount;
import edu.hei.school.agricultural.entity.MobileBankingAccount;
import org.springframework.stereotype.Component;

@Component
public class FinancialAccountDtoMapper {
    
    public FinancialAccount mapToDto(edu.hei.school.agricultural.entity.FinancialAccount financialAccount) {
        if (financialAccount instanceof CashAccount cashAccount) {
            return new edu.hei.school.agricultural.controller.dto.CashAccount(
                cashAccount.getId(),
                cashAccount.getAmount()
            );
        } else if (financialAccount instanceof MobileBankingAccount mobileAccount) {
            return new edu.hei.school.agricultural.controller.dto.MobileBankingAccount(
                mobileAccount.getId(),
                mobileAccount.getHolderName(),
                edu.hei.school.agricultural.controller.dto.MobileBankingService.valueOf(
                    mobileAccount.getMobileBankingService().name()
                ),
                mobileAccount.getMobileNumber(),
                mobileAccount.getAmount()
            );
        } else if (financialAccount instanceof BankAccount bankAccount) {
            return new edu.hei.school.agricultural.controller.dto.BankAccount(
                bankAccount.getId(),
                bankAccount.getHolderName(),
                edu.hei.school.agricultural.controller.dto.Bank.valueOf(
                    bankAccount.getBank().name()
                ),
                bankAccount.getBankCode(),
                bankAccount.getBankBranchCode(),
                bankAccount.getBankAccountNumber(),
                bankAccount.getBankAccountKey(),
                bankAccount.getAmount()
            );
        }
        throw new IllegalArgumentException("Unknown financial account type: " + financialAccount.getClass());
    }
}
