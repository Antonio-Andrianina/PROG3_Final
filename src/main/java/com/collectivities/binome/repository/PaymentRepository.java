package com.collectivities.binome.repository;

import com.collectivities.binome.controller.CreateMemberPayment;
import com.collectivities.binome.entity.Payment;
import com.collectivities.binome.entity.ActivityStatus;
import com.collectivities.binome.entity.PaymentMethod;
import com.collectivities.binome.exceptions.AppBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private final Connection conn;

    public Payment save(UUID memberId, CreateMemberPayment payment) {
        UUID id = UUID.randomUUID();
        String sql = """
            INSERT INTO payment (id, member_id, membership_fees_id, amount, payment_date, payment_method, reference, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.setObject(2, memberId);
            ps.setObject(3, UUID.fromString(payment.getMembershipFeeIdentifier()));
            ps.setDouble(4, payment.getAmount());
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            ps.setString(6, mapPaymentMode(payment.getPaymentMode()));
            ps.setString(7, payment.getAccountCreditedIdentifier());
            ps.setString(8, ActivityStatus.ACTIVE.name());
            ps.executeUpdate();

            return Payment.builder()
                    .id(id)
                    .memberId(memberId)
                    .membershipFeesId(UUID.fromString(payment.getMembershipFeeIdentifier()))
                    .amount(Double.valueOf(payment.getAmount()))
                    .paymentDate(LocalDate.now())
                    .paymentMethod(PaymentMethod.valueOf(mapPaymentMode(payment.getPaymentMode())))
                    .reference(payment.getAccountCreditedIdentifier())
                    .status(ActivityStatus.ACTIVE)
                    .build();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error saving payment: " + e.getMessage());
        }
    }

    private String mapPaymentMode(com.collectivities.binome.controller.PaymentMode mode) {
        switch (mode) {
            case CASH: return "CASH";
            case MOBILE_BANKING: return "MOBILE_MONEY";
            case BANK_TRANSFER: return "BANK_TRANSFER";
            default: return "CASH";
        }
    }
}