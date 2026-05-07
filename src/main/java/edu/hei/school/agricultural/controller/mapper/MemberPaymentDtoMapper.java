package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.CreateMemberPayment;
import edu.hei.school.agricultural.controller.dto.MemberPayment;
import edu.hei.school.agricultural.entity.MemberPayment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MemberPaymentDtoMapper {
    
    public MemberPayment mapToDto(MemberPayment payment) {
        return new MemberPayment(
            payment.getId(),
            payment.getAmount(),
            edu.hei.school.agricultural.controller.dto.PaymentMode.valueOf(
                payment.getPaymentMode().name()
            ),
            null, // accountCredited - would need to map from FinancialAccount
            payment.getCreationDate()
        );
    }
    
    public MemberPayment mapToEntity(CreateMemberPayment createPayment) {
        MemberPayment payment = new MemberPayment();
        payment.setAmount(createPayment.getAmount());
        payment.setPaymentMode(edu.hei.school.agricultural.entity.PaymentMode.valueOf(
            createPayment.getPaymentMode().name()
        ));
        payment.setCreationDate(LocalDate.now());
        return payment;
    }
}
