package edu.hei.school.agricultural.controller.dto;

import lombok.*;

@Data 
@Builder
@NoArgsConstructor 
@AllArgsConstructor 
@EqualsAndHashCode

public class CollectivityOverallStatistics {
    
    private CollectivityInformation collectivityInformation;
    private Integer newMembersNumber;
    private Double overallMemberCurrentDuePercentage;
}
