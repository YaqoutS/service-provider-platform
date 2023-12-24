package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Employee extends User {
    private double rating;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnore
    @ToString.Exclude
    private Company company; //if it is null, the employee doesn't belong to a company

    @Transient
    private String companyName;

    private int yearsOfExperience;

    @ManyToMany(mappedBy = "employees")
    @JsonIgnore
    @ToString.Exclude
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @ToString.Exclude
    private List<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @ToString.Exclude
    private List<ServiceFeedback> feedbacks = new ArrayList<>();


    public Employee(@NonNull String fullName, @NonNull @Size(min = 8, max = 30) String email, @NonNull String password, @NonNull boolean enabled) {
        super(fullName, email, password, enabled);
    }

//    public Employee(EmployeeDTO employeeDTO) {
//        super(employeeDTO.getFullName(), employeeDTO.getEmail(), employeeDTO.getPassword(), false);
//        this.setDateOfBirth(employeeDTO.getDateOfBirth());
//        this.rating = employeeDTO.getRating();
//    }

    public void addService(Service service) {
        services.add(service);
    }

    public void removeService(Service service) {
        services.remove(service);
    }

}
