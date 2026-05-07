package edu.hei.school.agricultural.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateMember extends MemberInformation {
    @NotBlank(message = "Collectivity identifier cannot be blank")
    private String collectivityIdentifier;
    
    private List<@NotBlank(message = "Referee ID cannot be blank") String> referees;
    
    @NotNull(message = "Registration fee paid status cannot be null")
    private Boolean registrationFeePaid;
    
    @NotNull(message = "Membership dues paid status cannot be null")
    private Boolean membershipDuesPaid;
}
