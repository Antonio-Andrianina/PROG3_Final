package edu.hei.school.agricultural.controller.dto;

import lombok.*;

@Data 
@Builder
@NoArgsConstructor 
@AllArgsConstructor 
@EqualsAndHashCode

public class MemberDescription {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String occupation;
}