package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeDTO extends UserDTO {
    String companyName;

    private double rating;

    private int yearsOfExperience;
}
