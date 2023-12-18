package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Company(id, name, employees, services, email,password, field, description, phone, location, ImagePath)
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Company {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private Admin admin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    private String field;

    private String description;

    private String phone;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "company")
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    @ToString.Exclude
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private List<Request> requests = new ArrayList<>();


    public Company(CompanyDTO companyDTO) {
        this.name = companyDTO.getName();
        this.image = companyDTO.getImage();
        this.field = companyDTO.getField();
        this.description = companyDTO.getDescription();
        this.phone = companyDTO.getPhone();
        this.address = companyDTO.getAddress();
    }

    public Company(String name) {
        this.name = name;
    }
}
