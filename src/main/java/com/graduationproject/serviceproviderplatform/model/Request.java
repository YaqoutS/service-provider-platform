package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Request {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    private Service service;

    @NonNull
    @OneToOne
    private Appointment appointment;

    @NonNull
    @ManyToOne
    private Employee employee;

    @NonNull
    @ManyToOne
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "feedback_id", referencedColumnName = "id")
    private ServiceFeedback feedback;

    @NonNull
    private String status; // suspended completed inComplete
    // If they employee refused the request, let's delete it immediately

    @ElementCollection
    private List<Integer> choices = new ArrayList<>();
}
