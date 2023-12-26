package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import lombok.*;

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
    @OneToOne(cascade = CascadeType.ALL)
    private Appointment appointment;

    @ManyToOne
    private Employee employee;

    @NonNull
    @ManyToOne
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "feedback_id", referencedColumnName = "id")
    private ServiceFeedback feedback;

    @NonNull
    private String status; // suspended completed inComplete
    // If the employee refuses the request, let's delete it immediately

    @ElementCollection
    private List<Integer> choices = new ArrayList<>();

    public Request(RequestDTO requestDTO) {
        this.appointment = requestDTO.getAppointment();
        this.feedback = requestDTO.getFeedback();
        this.status = requestDTO.getStatus();
        this.choices = requestDTO.getChoices();
    }
}
