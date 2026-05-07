package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.entity.Collectivity;
import edu.hei.school.agricultural.entity.CollectivityLocalStat;
import edu.hei.school.agricultural.entity.CollectivityOverallStat;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.AttendanceRepository;
import edu.hei.school.agricultural.repository.CollectivityRepository;
import edu.hei.school.agricultural.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final CollectivityRepository collectivityRepository;
    private final StatisticsRepository statisticsRepository;
    private final AttendanceRepository attendanceRepository;

    public List<CollectivityLocalStat> getLocalStatistics(String collectivityId,
                                                          LocalDate from,
                                                          LocalDate to) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException(
                        "Collectivity.id=" + collectivityId + " not found"));

        Map<String, Double> earned =
                statisticsRepository.getEarnedAmountByMember(collectivityId, from, to);
        Map<String, Double> unpaid =
                statisticsRepository.getUnpaidAmountByMember(collectivityId, from, to);
        Map<String, Double> assiduity =
                attendanceRepository.getAssiduityPercentageByMember(collectivityId, from, to);

        return collectivity.getMembers().stream()
                .map(member -> CollectivityLocalStat.builder()
                        .member(member)
                        .earnedAmount(earned.getOrDefault(member.getId(), 0.0))
                        .unpaidAmount(unpaid.getOrDefault(member.getId(), 0.0))
                        .assiduityPercentage(assiduity.getOrDefault(member.getId(), 0.0))
                        .build())
                .toList();
    }

    public List<CollectivityOverallStat> getOverallStatistics(LocalDate from, LocalDate to) {
        List<Collectivity> collectivities = collectivityRepository.findAll();

        Map<String, Integer> newMembers =
                statisticsRepository.getNewMembersCountByCollectivity(from, to);
        Map<String, Double> duePercentages =
                statisticsRepository.getCurrentDuePercentageByCollectivity(from, to);
        Map<String, Double> assiduityPercentages =
                attendanceRepository.getOverallAssiduityPercentageByCollectivity(from, to);

        return collectivities.stream()
                .map(collectivity -> CollectivityOverallStat.builder()
                        .collectivity(collectivity)
                        .newMembersNumber(newMembers.getOrDefault(collectivity.getId(), 0))
                        .overallMemberCurrentDuePercentage(
                                duePercentages.getOrDefault(collectivity.getId(), 0.0))
                        .overallMemberAssiduityPercentage(
                                assiduityPercentages.getOrDefault(collectivity.getId(), 0.0))
                        .build())
                .toList();
    }
}