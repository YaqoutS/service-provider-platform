package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToOne(cascade = CascadeType.ALL)
    private Appointment appointment;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Customer customer;

    private String status; // suspended completed inComplete
    // If the employee refuses the request, let's delete it immediately

    @NonNull
    private Long price;

    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "feedback_id", referencedColumnName = "id")
    private ServiceFeedback feedback;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<OptionChoice> optionChoices = new ArrayList<>();

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<InputChoice> inputChoices = new ArrayList<>();

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<SupplyChoice> supplyChoices = new ArrayList<>();

    public Request(RequestDTO requestDTO) {
        this.appointment = requestDTO.getAppointment();
        this.feedback = requestDTO.getFeedback();
        this.status = requestDTO.getStatus();
        this.price = requestDTO.getPrice();
        this.optionChoices = requestDTO.getOptionChoices();
        this.inputChoices = requestDTO.getInputChoices();
        this.supplyChoices = requestDTO.getSupplyChoices();
    }
    public List<OptionChoice> getOptionChoices() {
        return optionChoices.stream().filter(optionChoice -> optionChoice.getOption().getService() != null).collect(Collectors.toList());
    }
}
