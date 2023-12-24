package com.graduationproject.serviceproviderplatform.model;

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
    private Service service;
}
