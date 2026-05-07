package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.controller.dto.CreateActivity;
import edu.hei.school.agricultural.controller.dto.CreateActivityAttendance;
import edu.hei.school.agricultural.entity.Activity;
import edu.hei.school.agricultural.entity.ActivityAttendance;
import edu.hei.school.agricultural.entity.Collectivity;
import edu.hei.school.agricultural.entity.FinancialAccount;
import edu.hei.school.agricultural.entity.MembershipFee;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.ActivityAttendanceRepository;
import edu.hei.school.agricultural.repository.CollectivityTransactionRepository;
import edu.hei.school.agricultural.repository.FinancialAccountRepository;
import edu.hei.school.agricultural.repository.MembershipFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static edu.hei.school.agricultural.entity.ActivityStatus.ACTIVE;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class CollectivityService {
    private final CollectivityRepository collectivityRepository;
    private final MembershipFeeRepository membershipFeeRepository;
    private final FinancialAccountRepository financialAccountRepository;
    private final CollectivityTransactionRepository collectivityTransactionRepository;
    private final ActivityRepository activityRepository;
    private final ActivityAttendanceRepository activityAttendanceRepository;

    public List<Collectivity> createCollectivities(List<Collectivity> collectivities) {
        for (Collectivity collectivity : collectivities) {
            if (!collectivity.hasEnoughMembers()) {
                throw new BadRequestException("Collectivity must have at least 10 members, otherwise actual is " + collectivity.getMembers().size());
            }
            collectivity.setId(randomUUID().toString());
        }
        return collectivityRepository.saveAll(collectivities);
    }

    public Collectivity getCollectivityById(String id) {
        return collectivityRepository.findById(id).orElseThrow(() -> new NotFoundException("Collectivity.id= " + id + " not found"));
    }

    public Collectivity updateInformations(String collectivityId, String actualName, Integer actualNumber) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));
        if (actualNumber != null && collectivityRepository.isNumberExists(actualNumber)) {
            throw new BadRequestException("Collectivity.number=" + actualNumber + " already exists");
        }
        if (actualName != null && collectivityRepository.isNameExists(actualName)) {
            throw new BadRequestException("Collectivity.name=" + actualName + " already exists");
        }
        collectivity.setName(actualName);
        collectivity.setNumber(actualNumber);
        return collectivityRepository.saveAll(List.of((collectivity))).getFirst();
    }

    public List<MembershipFee> getMembershipFeesByCollectivityIdentifier(String collectivityIdentifier) {
        Collectivity collectivity = collectivityRepository.findById(collectivityIdentifier)
                .orElseThrow(() ->
                        new NotFoundException("Collectivity.id= " + collectivityIdentifier + " not found"));

        return membershipFeeRepository.getMembershipFeesByCollectivityId(collectivity.getId());
    }

    public List<MembershipFee> createMembershipFees(String collectivityIdentifier, List<MembershipFee> membershipFees) {
        Collectivity collectivity = collectivityRepository.findById(collectivityIdentifier)
                .orElseThrow(() ->
                        new NotFoundException("Collectivity.id= " + collectivityIdentifier + " not found"));
        for (MembershipFee membershipFee : membershipFees) {
            membershipFee.setId(randomUUID().toString());
            membershipFee.setStatus(ACTIVE);
            membershipFee.setCollectivityOwner(collectivity);
        }
        return membershipFeeRepository.saveAll(membershipFees);
    }

    public List<FinancialAccount> getFinancialAccounts(String collectivityId, LocalDate at) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));
        
        return financialAccountRepository.findByCollectivityId(collectivityId, at);
    }

    public List<CollectivityTransaction> getTransactions(String collectivityId, LocalDate from, LocalDate to) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));
        
        return collectivityTransactionRepository.findByCollectivityIdAndDateRange(collectivityId, from, to);
    }

    public List<Activity> createActivities(String collectivityId, List<CreateActivity> createActivities) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));
        
        List<Activity> activities = createActivities.stream()
                .map(createActivity -> {
                    Activity activity = new Activity();
                    activity.setId(randomUUID().toString());
                    activity.setName(createActivity.getName());
                    activity.setDescription(createActivity.getDescription());
                    activity.setActivityDate(createActivity.getActivityDate());
                    activity.setCollectivityId(collectivityId);
                    activity.setCreatedAt(java.time.LocalDateTime.now());
                    activity.setUpdatedAt(java.time.LocalDateTime.now());
                    return activity;
                })
                .toList();
        
        return activityRepository.saveAll(activities);
    }

    public List<Activity> getActivities(String collectivityId) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));
        
        return activityRepository.findByCollectivityId(collectivityId);
    }

    public List<ActivityAttendance> createActivityAttendance(String collectivityId, String activityId, 
                                                       CreateActivityAttendance createAttendance, String recordedBy) {
        // Verify collectivity exists
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));
        
        // Verify activity exists and belongs to collectivity
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity.id= " + activityId + " not found"));
        
        if (!activity.getCollectivityId().equals(collectivityId)) {
            throw new BadRequestException("Activity.id= " + activityId + " does not belong to collectivity.id= " + collectivityId);
        }
        
        // Check if any member already has attendance recorded for this activity
        for (String memberId : createAttendance.getPresentMemberIds()) {
            if (activityAttendanceRepository.existsByActivityIdAndMemberId(activityId, memberId)) {
                throw new BadRequestException("Member.id= " + memberId + " already has attendance recorded for activity.id= " + activityId);
            }
        }
        
        for (String memberId : createAttendance.getAbsentMemberIds()) {
            if (activityAttendanceRepository.existsByActivityIdAndMemberId(activityId, memberId)) {
                throw new BadRequestException("Member.id= " + memberId + " already has attendance recorded for activity.id= " + activityId);
            }
        }
        
        // Create attendance records
        List<ActivityAttendance> attendances = new java.util.ArrayList<>();
        
        // Add present members
        for (String memberId : createAttendance.getPresentMemberIds()) {
            ActivityAttendance attendance = new ActivityAttendance();
            attendance.setId(randomUUID().toString());
            attendance.setActivityId(activityId);
            attendance.setMemberId(memberId);
            attendance.setStatus(ActivityAttendance.AttendanceStatus.PRESENT);
            attendance.setRecordedAt(java.time.LocalDateTime.now());
            attendance.setRecordedBy(recordedBy);
            attendances.add(attendance);
        }
        
        // Add absent members
        for (String memberId : createAttendance.getAbsentMemberIds()) {
            ActivityAttendance attendance = new ActivityAttendance();
            attendance.setId(randomUUID().toString());
            attendance.setActivityId(activityId);
            attendance.setMemberId(memberId);
            attendance.setStatus(ActivityAttendance.AttendanceStatus.ABSENT);
            attendance.setRecordedAt(java.time.LocalDateTime.now());
            attendance.setRecordedBy(recordedBy);
            attendances.add(attendance);
        }
        
        return activityAttendanceRepository.saveAll(attendances);
    }

    public List<ActivityAttendance> getActivityAttendance(String collectivityId, String activityId) {
        // Verify collectivity exists
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));
        
        // Verify activity exists and belongs to collectivity
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity.id= " + activityId + " not found"));
        
        if (!activity.getCollectivityId().equals(collectivityId)) {
            throw new BadRequestException("Activity.id= " + activityId + " does not belong to collectivity.id= " + collectivityId);
        }
        
        // Return only present members as per requirement
        return activityAttendanceRepository.findByActivityIdAndStatus(activityId, ActivityAttendance.AttendanceStatus.PRESENT);
    }
}
