package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Company {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    private String name;

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
    @JsonIgnore
    @ToString.Exclude
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    @ToString.Exclude
    private Set<Employee> employees = new HashSet<>();

    public Company(CompanyDTO companyDTO) {
        this.name = companyDTO.getName();
        this.image = companyDTO.getImage();
        this.field = companyDTO.getField();
        this.description = companyDTO.getDescription();
        this.phone = companyDTO.getPhone();
        this.address = companyDTO.getAddress();
    }

//    public Company(String name) {
//        this.name = name;
//    }
}
