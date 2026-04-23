package com.collectivities.binome.repository;

import com.collectivities.binome.entity.Transaction;
import com.collectivities.binome.exceptions.AppBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final Connection conn;

    public List<Transaction> findByCollectivityIdAndDateRange(UUID collectivityId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT t.*, m.first_name, m.last_name 
            FROM transaction t
            JOIN member m ON m.id = t.member_id
            WHERE t.collectivity_id = ?
        """);

        if (startDate != null) {
            sql.append(" AND DATE(t.transaction_date) >= ?");
        }
        if (endDate != null) {
            sql.append(" AND DATE(t.transaction_date) <= ?");
        }
        sql.append(" ORDER BY t.transaction_date DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setObject(index++, collectivityId);
            if (startDate != null) {
                ps.setDate(index++, Date.valueOf(startDate));
            }
            if (endDate != null) {
                ps.setDate(index++, Date.valueOf(endDate));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding transactions: " + e.getMessage());
        }
        return transactions;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        return Transaction.builder()
                .id(UUID.fromString(rs.getString("id")))
                .collectivityId(UUID.fromString(rs.getString("collectivity_id")))
                .memberId(UUID.fromString(rs.getString("member_id")))
                .membershipFeesId(rs.getString("membership_fees_id") != null ? UUID.fromString(rs.getString("membership_fees_id")) : null)
                .transactionType(rs.getString("transaction_type"))
                .amount(rs.getDouble("amount"))
                .transactionDate(rs.getTimestamp("transaction_date") != null ? rs.getTimestamp("transaction_date").toLocalDateTime() : null)
                .description(rs.getString("description"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .memberFirstName(rs.getString("first_name"))
                .memberLastName(rs.getString("last_name"))
                .build();
    }
}