package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
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
    private Long serviceId;

    @Future
    private Date date;

    @NonNull
    private Long employeeId;

    @NonNull
    private Long customerId;

    @NonNull
    private String status;

    @ElementCollection
    private List<Integer> choices = new ArrayList<>();
}
