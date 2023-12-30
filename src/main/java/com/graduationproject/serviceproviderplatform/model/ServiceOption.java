package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ServiceOption {
    @Id @GeneratedValue
    private Long id;

    @NonNull @NotEmpty
    private String name;

    private String description;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Service service;
}
