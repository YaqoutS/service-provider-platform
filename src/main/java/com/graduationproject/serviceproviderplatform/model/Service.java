package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime lastUpdated;
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

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ServiceOption> serviceOptions = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ServiceInput> serviceInputs = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "services_supplies",
            joinColumns = @JoinColumn(name = "service_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "supply_id",referencedColumnName = "id")
    )
    private List<Supply> supplies = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Request> requests = new ArrayList<>();

    @JsonIgnore
    public List<Appointment> getAppointments() {
        return requests.stream()
                .filter(request -> !"Canceled".equals(request.getStatus()))
                .map(Request::getAppointment)
                .filter(appointment -> appointment != null && appointment.getStartDate() != null)
                .collect(Collectors.toList());
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    public void addSupply (Supply supply) {
        supplies.add(supply);
    }

    public void removeSupply (Supply supply) {
        supplies.remove(supply);
    }


    public void addSupplies(List<Supply> supplies) {
        supplies.addAll(supplies);
    }

}
