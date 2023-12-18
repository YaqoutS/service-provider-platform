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
    @ManyToMany(mappedBy = "employees")
    @JsonIgnore
    private Set<Service> services = new HashSet<>();

    // May I should delete it from here because it exists in the User class
    // I'll try that when adding the Requests APIs
    @OneToMany(mappedBy = "employee")
    private List<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private List<ServiceFeedback> feedbacks = new ArrayList<>();

    private double rating;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company; //if it is null, the employee doesn't belong to a company

    private int yearsOfExperience;

    public Employee(@NonNull String fullName, @NonNull @Size(min = 8, max = 30) String email, @NonNull String password, @NonNull boolean enabled) {
        super(fullName, email, password, enabled);
    }

    public Employee(EmployeeDTO employeeDTO) {
        super(employeeDTO.getFullName(), employeeDTO.getEmail(), employeeDTO.getPassword(), false);
        this.setDateOfBirth(employeeDTO.getDateOfBirth());
        this.rating = employeeDTO.getRating();
    }

    public void addService(Service service) {
        services.add(service);
    }

    public void addRequest(Request request) {
        requests.add(request);
    }
}
