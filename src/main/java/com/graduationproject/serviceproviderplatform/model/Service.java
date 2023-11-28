package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Service {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    private int yearsOfExperience;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_services",
            joinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id")
    )
    private List<Employee> employees = new ArrayList<>();

    private boolean isAvailable;

    @ManyToMany(mappedBy = "services")
    private List<Category> categories;

    @NonNull
    private Long avgPrice;

    @OneToMany(mappedBy = "service")
    @NonNull
    private List<ServiceOption> serviceOptions = new ArrayList<>();
}
