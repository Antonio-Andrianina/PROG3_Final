package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.controller.dto.CreateMemberPayment;
import edu.hei.school.agricultural.entity.Member;
import edu.hei.school.agricultural.entity.MemberPayment;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.MemberRepository;
import edu.hei.school.agricultural.repository.MemberPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberPaymentRepository memberPaymentRepository;

    public List<Member> addNewMembers(List<Member> memberList) {
        for (Member member : memberList) {
            if (!member.refereesAreEligible()) {
                throw new BadRequestException("Member.id=" + member.getId() + " member referees are not eligible");
            }
            if (!member.getMembershipDuesPaid()) {
                throw new BadRequestException("Member.id=" + member.getId() + " membership dues not paid");
            }
            if (!member.getRegistrationFeePaid()) {
                throw new BadRequestException("Member.id=" + member.getId() + " membership fees not paid");
            }
            member.setId(randomUUID().toString());
        }
        return memberRepository.saveAll(memberList);
    }

    public List<MemberPayment> createMemberPayments(String memberId, List<CreateMemberPayment> createMemberPayments) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member.id= " + memberId + " not found"));
        
        List<MemberPayment> payments = createMemberPayments.stream()
                .map(createPayment -> {
                    MemberPayment payment = new MemberPayment();
                    payment.setId(randomUUID().toString());
                    payment.setAmount(createPayment.getAmount());
                    payment.setPaymentMode(edu.hei.school.agricultural.entity.PaymentMode.valueOf(
                        createPayment.getPaymentMode().name()
                    ));
                    payment.setAccountCreditedId(createPayment.getAccountCreditedIdentifier());
                    payment.setCreationDate(java.time.LocalDate.now());
                    payment.setMemberId(memberId);
                    return payment;
                })
                .toList();
        
        return memberPaymentRepository.saveAll(payments);
    }
}
