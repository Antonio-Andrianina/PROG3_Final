package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.entity.FinancialAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FinancialAccountRepository {
    private final Connection connection;

    public List<FinancialAccount> findByCollectivityId(String collectivityId, LocalDate at) {
        List<FinancialAccount> accounts = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT id, account_type, amount, holder_name, bank_name, bank_code, bank_branch_code, 
                       bank_account_number, bank_account_key, mobile_banking_service, mobile_number
                FROM financial_account 
                WHERE collectivity_id = ?
                AND (created_at <= ? OR ? IS NULL)
                ORDER BY created_at DESC
                """)) {
            
            preparedStatement.setString(1, collectivityId);
            if (at != null) {
                preparedStatement.setDate(2, java.sql.Date.valueOf(at));
                preparedStatement.setDate(3, java.sql.Date.valueOf(at));
            } else {
                preparedStatement.setNull(2, java.sql.Types.DATE);
                preparedStatement.setNull(3, java.sql.Types.DATE);
            }
            
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accounts.add(mapFromResultSet(resultSet));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return accounts;
    }
    
    private FinancialAccount mapFromResultSet(ResultSet resultSet) throws SQLException {
        String accountType = resultSet.getString("account_type");
        
        return switch (accountType) {
            case "CASH" -> new CashAccount(
                resultSet.getString("id"),
                resultSet.getBigDecimal("amount")
            );
            case "MOBILE_BANKING" -> new MobileBankingAccount(
                resultSet.getString("id"),
                resultSet.getString("holder_name"),
                edu.hei.school.agricultural.entity.MobileBankingService.valueOf(
                    resultSet.getString("mobile_banking_service")
                ),
                resultSet.getInt("mobile_number"),
                resultSet.getBigDecimal("amount")
            );
            case "BANK" -> new BankAccount(
                resultSet.getString("id"),
                resultSet.getString("holder_name"),
                edu.hei.school.agricultural.entity.Bank.valueOf(resultSet.getString("bank_name")),
                resultSet.getInt("bank_code"),
                resultSet.getInt("bank_branch_code"),
                resultSet.getInt("bank_account_number"),
                resultSet.getInt("bank_account_key"),
                resultSet.getBigDecimal("amount")
            );
            default -> throw new IllegalArgumentException("Unknown account type: " + accountType);
        };
    }
}
