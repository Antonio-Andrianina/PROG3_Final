package com.collectivities.binome.service;

import com.collectivities.binome.entity.Transaction;
import com.collectivities.binome.exceptions.AppBadRequestException;
import com.collectivities.binome.repository.CollectivityRepository;
import com.collectivities.binome.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CollectivityRepository collectivityRepository;

    public List<Transaction> getTransactions(String collectivityId, LocalDate startDate, LocalDate endDate) {
        UUID id = UUID.fromString(collectivityId);

        if (!collectivityRepository.existsById(id)) {
            throw new AppBadRequestException("Collectivity not found: " + collectivityId);
        }

        return transactionRepository.findByCollectivityIdAndDateRange(id, startDate, endDate);
    }
}