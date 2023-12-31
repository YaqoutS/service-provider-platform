package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeDTO extends UserDTO {
    String companyName;

    private double rating;

    private int yearsOfExperience;

    private Set<DayOfWeek> workDays;

    private LocalTime workStartTime;

    private LocalTime workEndTime;
}
