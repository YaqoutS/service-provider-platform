package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Appointment {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private LocalDate endDate;
    @NonNull
    private LocalTime startTime;

    @NonNull
    private LocalTime endTime;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
}
