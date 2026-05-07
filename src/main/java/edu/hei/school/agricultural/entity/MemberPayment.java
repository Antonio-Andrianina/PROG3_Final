package edu.hei.school.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberPayment {
    private String id;
    private BigDecimal amount;
    private PaymentMode paymentMode;
    private String accountCreditedId;
    private LocalDate creationDate;
    private String memberId;
}
