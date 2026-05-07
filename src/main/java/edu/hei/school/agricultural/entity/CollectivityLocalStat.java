package edu.hei.school.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectivityLocalStat {
    private Member member;
    private Double earnedAmount;
    private Double unpaidAmount;
    private Double assiduityPercentage;
}