package com.collectivities.binome.entity;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Member extends MemberInformation {
    private String id;
    private List<String> referees;
}
