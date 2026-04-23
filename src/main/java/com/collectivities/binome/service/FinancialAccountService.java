package com.collectivities.binome.service;

import com.collectivities.binome.entity.*;
import com.collectivities.binome.exceptions.AppBadRequestException;
import com.collectivities.binome.repository.CollectivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialAccountService {

    private final CollectivityRepository collectivityRepository;

    public List<FinancialAccount> getAccountsWithBalance(String collectivityId, LocalDate asOfDate) {
        UUID id = UUID.fromString(collectivityId);

        if (!collectivityRepository.existsById(id)) {
            throw new AppBadRequestException("Collectivity not found: " + collectivityId);
        }

        LocalDate balanceDate = asOfDate != null ? asOfDate : LocalDate.now();

        List<FinancialAccount> accounts = new ArrayList<>();

        CashAccount cashAccount = new CashAccount();
        cashAccount.setId(UUID.randomUUID().toString());
        cashAccount.setAmount(calculateCashBalance(id, balanceDate));
        accounts.add(cashAccount);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setAccountNumber("123456789");
        bankAccount.setAccountName("Main Bank Account");
        bankAccount.setAmount(calculateBankBalance(id, balanceDate));
        accounts.add(bankAccount);

        MobileBankingAccount mobileAccount = new MobileBankingAccount();
        mobileAccount.setId(UUID.randomUUID().toString());
        mobileAccount.setPhoneNumber("+261321234567");
        mobileAccount.setProvider("MVola");
        mobileAccount.setAmount(calculateMobileBalance(id, balanceDate));
        accounts.add(mobileAccount);

        return accounts;
    }

    private Double calculateCashBalance(UUID collectivityId, LocalDate asOfDate) {
        return 150000.0;
    }

    private Double calculateBankBalance(UUID collectivityId, LocalDate asOfDate) {
        return 2500000.0;
    }

    private Double calculateMobileBalance(UUID collectivityId, LocalDate asOfDate) {
        return 75000.0;
    }
}