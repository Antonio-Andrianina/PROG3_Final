package com.collectivities.binome.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Member extends MemberInformation {
    private String id;
    private List<String> referees;
    private LocalDate registrationDate;
    private Collectivity collectivity;
}