package edu.hei.school.agricultural.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateCollectivity {
    @NotBlank(message = "Location cannot be blank")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;
    
    @NotNull(message = "Members list cannot be null")
    private List<@NotBlank(message = "Member ID cannot be blank") String> members;
    
    @NotNull(message = "Federation approval cannot be null")
    private Boolean federationApproval;
    
    @NotNull(message = "Structure cannot be null")
    private CreateCollectivityStructure structure;
}
