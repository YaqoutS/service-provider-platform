package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ServiceFeedback {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    private Customer customer;

    @NonNull
    @ManyToOne
    private Employee employee;

    @NonNull @Min(1) @Max(5)
    private Long rating;

    private String description;

    private LocalDate date;
}
