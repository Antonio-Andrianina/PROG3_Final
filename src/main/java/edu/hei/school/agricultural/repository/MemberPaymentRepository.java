package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.entity.MemberPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberPaymentRepository {
    private final Connection connection;

    public List<MemberPayment> saveAll(List<MemberPayment> payments) {
        List<MemberPayment> savedPayments = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                insert into member_payment (id, amount, payment_mode, account_credited_id, creation_date, member_id)
                values (?, ?, ?, ?, ?, ?)
                """)) {
            
            for (MemberPayment payment : payments) {
                preparedStatement.setString(1, payment.getId());
                preparedStatement.setBigDecimal(2, payment.getAmount());
                preparedStatement.setString(3, payment.getPaymentMode().name());
                preparedStatement.setString(4, payment.getAccountCreditedId());
                preparedStatement.setDate(5, java.sql.Date.valueOf(payment.getCreationDate()));
                preparedStatement.setString(6, payment.getMemberId());
                preparedStatement.addBatch();
            }
            
            preparedStatement.executeBatch();
            
            // Return the saved payments
            for (MemberPayment payment : payments) {
                savedPayments.add(findById(payment.getId()).orElseThrow());
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return savedPayments;
    }
    
    public java.util.Optional<MemberPayment> findById(String id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                select id, amount, payment_mode, account_credited_id, creation_date, member_id
                from member_payment
                where id = ?
                """)) {
            
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return java.util.Optional.of(mapFromResultSet(resultSet));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return java.util.Optional.empty();
    }
    
    private MemberPayment mapFromResultSet(ResultSet resultSet) throws SQLException {
        MemberPayment payment = new MemberPayment();
        payment.setId(resultSet.getString("id"));
        payment.setAmount(resultSet.getBigDecimal("amount"));
        payment.setPaymentMode(edu.hei.school.agricultural.entity.PaymentMode.valueOf(
            resultSet.getString("payment_mode")
        ));
        payment.setAccountCreditedId(resultSet.getString("account_credited_id"));
        payment.setCreationDate(resultSet.getDate("creation_date").toLocalDate());
        payment.setMemberId(resultSet.getString("member_id"));
        return payment;
    }
}
