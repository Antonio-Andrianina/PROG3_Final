package com.collectivities.binome.service;

import java.util.ArrayList;
import java.util.List;

import com.collectivities.binome.controller.CreateMembershipFee;
import com.collectivities.binome.controller.MembershipFee;
import com.collectivities.binome.repository.MembershipFeesRepository;


public class MembershipFeesService {

     private final MembershipFeesRepository repository;

    public MembershipFeesService(MembershipFeesRepository repository) {
        this.repository = repository;
    }

    public List<MembershipFee> getFees(String collectivityId) {
        return repository.getByCollectivityId(collectivityId);
    }

    public List<MembershipFee> createFees(String collectivityId, List<CreateMembershipFee> dtos) {
        List<String> createdFeesId = new ArrayList<>();

        for(CreateMembershipFee fee : dtos){
            createdFeesId.add(
                    this.repository.save(fee, collectivityId)
            );
        }

        List<MembershipFee> savedFees = new ArrayList<>();

        for(String id : createdFeesId){
            savedFees.add(
                    this.repository.getById(id)
            );
        }

        return savedFees;
    }

}
