package com.collectivities.binome.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class MembershipFees extends CreateMembershipFee{
    private String id;
    private ActivityStatus status;
}
