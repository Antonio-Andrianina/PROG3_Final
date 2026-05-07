package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.controller.dto.CollectivityLocalStatistics;
import edu.hei.school.agricultural.controller.dto.CollectivityOverallStatistics;
import edu.hei.school.agricultural.controller.dto.CollectivityInformation;
import edu.hei.school.agricultural.controller.dto.MemberDescription;
import edu.hei.school.agricultural.entity.Collectivity;
import edu.hei.school.agricultural.entity.CollectivityLocalStat;
import edu.hei.school.agricultural.entity.CollectivityOverallStat;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.CollectivityRepository;
import edu.hei.school.agricultural.repository.MemberRepository;
import edu.hei.school.agricultural.repository.StatisticsRepository;
import edu.hei.school.agricultural.repository.MembershipFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final StatisticsRepository statisticsRepository;
    private final MembershipFeeRepository membershipFeeRepository;

    public List<CollectivityLocalStatistics> getLocalStatistics(
            String collectivityId, LocalDate from, LocalDate to) {

        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id=" + collectivityId + " not found"));

        Map<String, Double> earned = statisticsRepository.getEarnedAmountByMember(collectivityId, from, to);
        Map<String, Double> unpaid = statisticsRepository.getUnpaidAmountByMember(collectivityId, from, to);

        return collectivity.getMembers().stream()
                .map(member -> CollectivityLocalStatistics.builder()
                        .memberDescription(MemberDescription.builder()
                                .id(member.getId())
                                .firstName(member.getFirstName())
                                .lastName(member.getLastName())
                                .email(member.getEmail())
                                .occupation(member.getOccupation() == null ? null : member.getOccupation().name())
                                .build())
                        .earnedAmount(earned.getOrDefault(member.getId(), 0.0))
                        .unpaidAmount(unpaid.getOrDefault(member.getId(), 0.0))
                        .build())
                .collect(Collectors.toList());
    }

    public List<CollectivityOverallStatistics> getOverallStatistics(LocalDate from, LocalDate to) {
        List<CollectivityOverallStat> stats = statisticsRepository.getOverallStats(from, to);
        
        return stats.stream()
                .map(stat -> CollectivityOverallStatistics.builder()
                        .collectivityInformation(CollectivityInformation.builder()
                                .name(stat.getCollectivity().getName())
                                .number(stat.getCollectivity().getNumber())
                                .build())
                        .newMembersNumber(stat.getNewMembersNumber())
                        .overallMemberCurrentDuePercentage(stat.getOverallMemberCurrentDuePercentage())
                        .build())
                .collect(Collectors.toList());
    }
}