package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    private int price;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Service service;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    private List<OptionChoice> optionChoices = new ArrayList<>();
}
