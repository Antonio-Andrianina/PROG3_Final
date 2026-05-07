package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.CollectivityTransaction;
import org.springframework.stereotype.Component;

@Component
public class CollectivityTransactionDtoMapper {
    
    public CollectivityTransaction mapToDto(CollectivityTransaction transaction) {
        return new CollectivityTransaction(
            transaction.getId(),
            transaction.getCreationDate(),
            transaction.getAmount(),
            edu.hei.school.agricultural.controller.dto.PaymentMode.valueOf(
                transaction.getPaymentMode().name()
            ),
            null, // accountCredited - would need to map from FinancialAccount
            null  // memberDebited - would need to map from Member
        );
    }
}
