package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor

public class RequestDTO {
    private Long id;

    @NonNull
    private Long serviceId;

    @NonNull
    private Appointment appointment;

    private Long employeeId;

    @NonNull
    private Long customerId;

    private ServiceFeedback feedback;

    private String status; // suspended completed inComplete

    @ElementCollection
    private List<Integer> choices = new ArrayList<>();
}
