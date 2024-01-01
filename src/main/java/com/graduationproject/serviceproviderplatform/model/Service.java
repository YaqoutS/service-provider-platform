package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Service {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private int type; // To specify if this service is of type 1 or type 2.

    @NonNull
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    @JoinTable(
            name = "employee_services",
            joinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id")
    )
    private Set<Employee> employees = new HashSet<>(); // If the service belong to a freelancer, This list will only have one employee

    @NonNull
    private boolean isAvailable;

    @NonNull
    private Long avgPrice;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ServiceOption> serviceOptions = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ServiceInput> serviceInputs = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Request> requests = new ArrayList<>();

    @JsonIgnore
    public List<Appointment> getAppointments() {
        return requests.stream()
                .map(Request::getAppointment)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }
}
