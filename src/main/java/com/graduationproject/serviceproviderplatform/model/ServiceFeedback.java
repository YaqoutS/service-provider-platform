package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Customer customer;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Employee employee;

    @NonNull @Min(1) @Max(5)
    private Long rating;

    private String description;

    private LocalDate date;
}
