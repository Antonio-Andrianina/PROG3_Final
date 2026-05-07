package edu.hei.school.agricultural.repository;

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
public class CollectivityTransactionRepository {
    private final Connection connection;

    public List<CollectivityTransaction> findByCollectivityIdAndDateRange(String collectivityId, LocalDate from, LocalDate to) {
        List<CollectivityTransaction> transactions = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT id, creation_date, amount, payment_mode, account_credited_id, member_debited_id
                FROM collectivity_transaction 
                WHERE collectivity_id = ?
                AND creation_date >= ?
                AND creation_date <= ?
                ORDER BY creation_date DESC
                """)) {
            
            preparedStatement.setString(1, collectivityId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(from));
            preparedStatement.setDate(3, java.sql.Date.valueOf(to));
            
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactions.add(mapFromResultSet(resultSet));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return transactions;
    }
    
    private CollectivityTransaction mapFromResultSet(ResultSet resultSet) throws SQLException {
        CollectivityTransaction transaction = new CollectivityTransaction();
        transaction.setId(resultSet.getString("id"));
        transaction.setCreationDate(resultSet.getDate("creation_date").toLocalDate());
        transaction.setAmount(resultSet.getBigDecimal("amount"));
        transaction.setPaymentMode(edu.hei.school.agricultural.entity.PaymentMode.valueOf(
            resultSet.getString("payment_mode")
        ));
        // Note: accountCredited and memberDebited would need to be fetched separately
        // or joined in the query if needed
        return transaction;
    }
}
