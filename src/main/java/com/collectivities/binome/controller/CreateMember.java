package com.collectivities.binome.controller;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.collectivities.binome.entity.MemberInformation;

@Getter
@Setter
public class CreateMember extends MemberInformation {
    private String collectivityIdentifier;
    private List<String> referees;
    private Boolean registrationFeePaid;
    private Boolean membershipDuesPaid;
}
