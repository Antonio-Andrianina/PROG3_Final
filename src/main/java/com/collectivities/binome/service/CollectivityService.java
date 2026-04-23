package com.collectivities.binome.service;

import com.collectivities.binome.controller.AssignCollectivityIdentity;
import com.collectivities.binome.entity.*;
import com.collectivities.binome.exceptions.AppBadRequestException;
import com.collectivities.binome.repository.CollectivityRepository;
import com.collectivities.binome.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public Collectivity save(CreateCollectivity toSave) {
        List<Member> members = new ArrayList<>();

        for (String id : toSave.getMembers()) {
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new AppBadRequestException("Member not found: " + id));
            members.add(member);
        }

        if (members.size() < 10 || toSave.getFederationApproval() == null || !toSave.getFederationApproval()) {
            throw new AppBadRequestException("Collectivity must have at least 10 members and federation approval.");
        }

        int president = 0;
        int vicePresident = 0;
        int treasurer = 0;
        int secretary = 0;

        for (Member member : members) {
            if (member.getOccupation() != null) {
                switch (member.getOccupation()) {
                    case SECRETARY -> secretary++;
                    case TREASURER -> treasurer++;
                    case VICE_PRESIDENT -> vicePresident++;
                    case PRESIDENT -> president++;
                    default -> {}
                }
            }
        }

        if (president != 1 || vicePresident != 1 || treasurer != 1 || secretary != 1) {
            throw new AppBadRequestException("Collectivity must have exactly one president, vice-president, treasurer, and secretary.");
        }

        List<Long> seniorityOfMembers = new ArrayList<>();
        for (Member member : members) {
            seniorityOfMembers.add(memberService.getSeniority(member.getId()));
        }

        long memberWithEnoughSeniority = seniorityOfMembers.stream()
                .filter(s -> s > 180)
                .count();

        if (memberWithEnoughSeniority < 5) {
            throw new AppBadRequestException("Collectivity must have at least 5 members with 6+ months seniority.");
        }

        Collectivity collectivity = collectivityRepository.createCollectivity(toSave);

        for (Member member : members) {
            memberRepository.attachMember(member.getId(), collectivity.getId(), member.getOccupation());
        }

        collectivity.setMembers(members);

        CollectivityStructure structure = new CollectivityStructure();
        for (Member member : members) {
            if (member.getOccupation() == MemberOccupation.PRESIDENT) {
                structure.setPresident(member);
            } else if (member.getOccupation() == MemberOccupation.VICE_PRESIDENT) {
                structure.setVicePresident(member);
            } else if (member.getOccupation() == MemberOccupation.TREASURER) {
                structure.setTreasurer(member);
            } else if (member.getOccupation() == MemberOccupation.SECRETARY) {
                structure.setSecretary(member);
            }
        }
        collectivity.setStructure(structure);

        return collectivity;
    }

    public List<Collectivity> saveAll(List<CreateCollectivity> toSave) {
        List<Collectivity> collectivities = new ArrayList<>();
        for (CreateCollectivity collectivity : toSave) {
            collectivities.add(this.save(collectivity));
        }
        return collectivities;
    }

    public Collectivity findById(String id) {
        UUID uuid = UUID.fromString(id);
        return collectivityRepository.findById(uuid)
                .orElseThrow(() -> new AppBadRequestException("Collectivity not found: " + id));
    }

    public Collectivity assignIdentity(String id, AssignCollectivityIdentity body) {
        UUID uuid = UUID.fromString(id);

        if (!collectivityRepository.existsById(uuid)) {
            throw new AppBadRequestException("Collectivity not found: " + id);
        }

        String number = body.getNumber() != null ? body.getNumber().toString() : null;
        String name = body.getName();

        if (name != null && collectivityRepository.existsByUniqueName(name)) {
            Collectivity existing = collectivityRepository.findByUniqueName(name).orElse(null);
            if (existing != null && !existing.getId().equals(id)) {
                throw new AppBadRequestException("Unique name already exists for another collectivity: " + name);
            }
        }

        return collectivityRepository.assignIdentity(uuid, number, name);
    }
}