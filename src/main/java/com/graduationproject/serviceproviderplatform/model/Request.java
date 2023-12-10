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
public class Request extends Auditable {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    private Service service;

    @Future
    private LocalDateTime date;

    @ManyToOne
    private Company company;

    @NonNull
    @ManyToOne
    private Employee employee;

    @NonNull
    @ManyToOne
    private Customer customer;

    @OneToOne
    private ServiceFeedback feedback;

    @NonNull
    private String status; //completed inComplete

    @ElementCollection
    private List<Integer> choices = new ArrayList<>();
}
