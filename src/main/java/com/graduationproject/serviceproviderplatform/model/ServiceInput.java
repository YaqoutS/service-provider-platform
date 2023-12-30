package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "input", cascade = CascadeType.ALL)
    private List<InputChoice> inputChoices = new ArrayList<>();
}
