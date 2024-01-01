package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    private Set<DayOfWeek> workDays;

    private LocalTime workStartTime;

    private LocalTime workEndTime;

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

    public Employee(String name, String email, String password, String confirmPassword, boolean enabled, Company company, int yearsOfExperience, Address address, String phone) {
        super(name, email, password, confirmPassword, enabled, address, phone);
        this.company = company;
        this.yearsOfExperience = yearsOfExperience;
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

    @JsonIgnore
    public List<Appointment> getAppointments() {
        return requests.stream()
                .map(Request::getAppointment)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Set<DayOfWeek> getWorkDays() {
        Set<DayOfWeek> defaultWorkDays = EnumSet.allOf(DayOfWeek.class);
        defaultWorkDays.remove(DayOfWeek.FRIDAY);
        return Optional.ofNullable(company)                     // Wrap company in Optional
                .map(Company::getWorkDays)                      // If company is not null, get its work days
                .orElseGet(() -> Optional.ofNullable(workDays)  // If company or its work days are null, try workDays
                        .orElse(defaultWorkDays)                // If workDays is null, return defaultWorkDays
                );
    }

    public LocalTime getWorkStartTime() {
        if (company != null) return company.getWorkStartTime();
        return workStartTime;
    }

    public LocalTime getWorkEndTime() {
        if (company != null) return company.getWorkEndTime();
        return workEndTime;
    }
}
