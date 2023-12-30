package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ServiceInput {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    private String description;

    private boolean isRequired;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Service service;
}
