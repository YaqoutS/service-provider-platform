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

//    @OneToMany(mappedBy = "employee")
//    private List<ServiceFeedback> feedbacks = new ArrayList<>();


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


//    public void updateRating() {
//        // Calculate the average rating from all feedbacks in the requests
//        if (requests != null && !requests.isEmpty()) {
//            double totalRating = requests.stream()
//                    .filter(request -> request.getFeedback() != null)
//                    .mapToDouble(request -> request.getFeedback().getRating())
//                    .sum();
//
//            rating = totalRating / getTotalRequestsCount()  ;
//        } else {
//            // No requests, set the rating to a default value or handle accordingly
//            rating = 0.0;
//        }
//    }
//
//    private int getTotalRequestsCount() {
//        return requests.stream()
//                .filter(request -> request.getFeedback() != null)
//                .collect(Collectors.toList()).size();
//    }

    public double calculateAverageRating() {
        return requests.stream()
                .filter(request -> request.getFeedback() != null)
                .mapToDouble(request -> request.getFeedback().getRating())
                .average()
                .orElse(0.0);
    }

    public List<ServiceFeedback> getFeedbacks() {
        List<ServiceFeedback> feedbacks = new ArrayList<>();
        if (requests != null && !requests.isEmpty()) {
            feedbacks = requests.stream()
                    .filter(request -> request.getFeedback() != null)
                    .map(request -> request.getFeedback())
                    .collect(Collectors.toList());

        }
        return feedbacks;
    }

    @JsonIgnore
    public List<Appointment> getAppointments() {
        if (requests.isEmpty()) return Collections.emptyList();
        return requests.stream()
                .filter(request -> !"Canceled".equals(request.getStatus()))
                .map(Request::getAppointment)
                .filter(appointment -> appointment != null && appointment.getStartDate() != null)
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
