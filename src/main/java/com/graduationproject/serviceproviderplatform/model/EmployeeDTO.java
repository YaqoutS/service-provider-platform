package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class EmployeeDTO extends UserDTO {
    private double rating;

    @NonNull
    private boolean isAvailable;

    private int yearsOfExperience;
}
