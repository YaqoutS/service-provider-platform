package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    private Company company; // If this is null, that means it belong to a freelancer not to a company

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_services",
            joinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id")
    )
    private Set<Employee> employees = new HashSet<>(); // If the service belong to a freelancer, This list will only have one employee

    private boolean isAvailable;


    @NonNull
    private Long avgPrice;

    @OneToMany(mappedBy = "service")
    //@NonNull
    private List<ServiceOption> serviceOptions = new ArrayList<>();

    @OneToMany(mappedBy = "service")
    private List<Request> requests = new ArrayList<>();

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
}
