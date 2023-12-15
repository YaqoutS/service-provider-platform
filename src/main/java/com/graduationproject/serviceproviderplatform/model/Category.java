package com.graduationproject.serviceproviderplatform.model;

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
public class Category {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    // This will be null if the main admin is the one who added this category,
    // and freelancers can add services only to the categories added by the main admin (which company is null)
    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "category")
    private Set<Service> services = new HashSet<>();

    public void addService(Service service) {
        services.add(service);
    }
}
