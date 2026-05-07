package edu.hei.school.agricultural.mapper;

import edu.hei.school.agricultural.controller.dto.CollectivityInformation;
import edu.hei.school.agricultural.controller.dto.CollectivityLocalStatistics;
import edu.hei.school.agricultural.controller.dto.CollectivityOverallStatistics;
import edu.hei.school.agricultural.controller.dto.MemberDescription;
import org.springframework.stereotype.Component;

@Component
public class StatisticsDtoMapper {

    public CollectivityLocalStatistics mapToDto(CollectivityLocalStat stat) {
        return CollectivityLocalStatistics.builder()
                .memberDescription(MemberDescription.builder()
                        .id(stat.getMember().getId())
                        .firstName(stat.getMember().getFirstName())
                        .lastName(stat.getMember().getLastName())
                        .email(stat.getMember().getEmail())
                        .occupation(stat.getMember().getOccupation() == null
                                ? null : stat.getMember().getOccupation().name())
                        .build())
                .earnedAmount(stat.getEarnedAmount())
                .unpaidAmount(stat.getUnpaidAmount())
                .assiduityPercentage(stat.getAssiduityPercentage())
                .build();
    }

    public CollectivityOverallStatistics mapToDto(CollectivityOverallStat stat) {
        return CollectivityOverallStatistics.builder()
                .collectivityInformation(CollectivityInformation.builder()
                        .name(stat.getCollectivity().getName())
                        .number(stat.getCollectivity().getNumber())
                        .build())
                .newMembersNumber(stat.getNewMembersNumber())
                .overallMemberCurrentDuePercentage(stat.getOverallMemberCurrentDuePercentage())
                .overallMemberAssiduityPercentage(stat.getOverallMemberAssiduityPercentage())
                .build();
    }
}