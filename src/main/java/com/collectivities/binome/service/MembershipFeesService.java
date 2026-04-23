package com.collectivities.binome.service;

import com.collectivities.binome.entity.CreateMembershipFees;
import com.collectivities.binome.entity.MembershipFees;
import com.collectivities.binome.exceptions.AppBadRequestException;
import com.collectivities.binome.repository.CollectivityRepository;
import com.collectivities.binome.repository.MembershipFeesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipFeesService {

    private final MembershipFeesRepository repository;
    private final CollectivityRepository collectivityRepository;

    public List<MembershipFees> getFees(String collectivityId) {
        UUID id = UUID.fromString(collectivityId);

        if (!collectivityRepository.existsById(id)) {
            throw new AppBadRequestException("Collectivity not found: " + collectivityId);
        }

        return repository.getByCollectivityId(collectivityId);
    }

    public List<MembershipFees> createFees(String collectivityId, List<CreateMembershipFees> dtos) {
        UUID id = UUID.fromString(collectivityId);

        if (!collectivityRepository.existsById(id)) {
            throw new AppBadRequestException("Collectivity not found: " + collectivityId);
        }

        List<String> createdFeesId = new ArrayList<>();

        for (CreateMembershipFees fee : dtos) {
            createdFeesId.add(repository.save(fee, collectivityId));
        }

        List<MembershipFees> savedFees = new ArrayList<>();

        for (String feeId : createdFeesId) {
            savedFees.add(repository.getById(feeId));
        }

        return savedFees;
    }
}