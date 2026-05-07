package edu.hei.school.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectivityOverallStat {
    private Collectivity collectivity;
    private Integer newMembersNumber;
    private Double overallMemberCurrentDuePercentage;
    private Double overallMemberAssiduityPercentage;
}