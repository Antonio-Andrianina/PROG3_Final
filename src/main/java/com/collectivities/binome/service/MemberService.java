package com.collectivities.binome.service;

import com.collectivities.binome.controller.CreateMemberPayment;
import com.collectivities.binome.entity.CreateMember;
import com.collectivities.binome.entity.Member;
import com.collectivities.binome.entity.Payment;
import com.collectivities.binome.exceptions.AppBadRequestException;
import com.collectivities.binome.repository.MemberRepository;
import com.collectivities.binome.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;

    public Long getSeniority(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new AppBadRequestException("Member not found: " + id));

        if (member.getRegistrationDate() == null) {
            return 0L;
        }

        return ChronoUnit.DAYS.between(member.getRegistrationDate(), LocalDate.now());
    }

    public List<Member> saveAll(List<CreateMember> toSave) {
        List<Member> members = new ArrayList<>();
        for (CreateMember member : toSave) {
            // Validation des paiements
            if (member.getRegistrationFeePaid() == null || !member.getRegistrationFeePaid()) {
                throw new AppBadRequestException("Registration fee not paid for member: " + member.getFirstName());
            }
            if (member.getMembershipDuesPaid() == null || !member.getMembershipDuesPaid()) {
                throw new AppBadRequestException("Membership dues not paid for member: " + member.getFirstName());
            }

            // Validation des referees
            if (member.getReferees() != null && !member.getReferees().isEmpty()) {
                for (String refereeId : member.getReferees()) {
                    if (memberRepository.findById(refereeId).isEmpty()) {
                        throw new AppBadRequestException("Referee not found: " + refereeId);
                    }
                }
            }

            members.add(memberRepository.save(member));
        }
        return members;
    }

    public List<Payment> createPayments(String memberId, List<CreateMemberPayment> payments) {
        UUID id = UUID.fromString(memberId);

        if (!memberRepository.existsById(id)) {
            throw new AppBadRequestException("Member not found: " + memberId);
        }

        List<Payment> recorded = new ArrayList<>();
        for (CreateMemberPayment payment : payments) {
            if (payment.getAmount() == null || payment.getAmount() <= 0) {
                throw new AppBadRequestException("Payment amount must be positive");
            }
            if (payment.getMembershipFeeIdentifier() == null) {
                throw new AppBadRequestException("Membership fee identifier is required");
            }
            if (payment.getPaymentMode() == null) {
                throw new AppBadRequestException("Payment mode is required");
            }

            Payment newPayment = paymentRepository.save(id, payment);
            recorded.add(newPayment);
        }

        return recorded;
    }
}