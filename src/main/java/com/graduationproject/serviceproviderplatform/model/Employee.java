package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Employee extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "employees")
    private Set<Service> services = new HashSet<>();

    private double rating;

    private boolean isAvailable;

    private String company; //if it is null, the employee doesn't belong to a company
}
