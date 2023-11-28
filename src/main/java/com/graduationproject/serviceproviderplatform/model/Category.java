package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "categories_services",
            joinColumns = @JoinColumn(name = "category_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "service_id",referencedColumnName = "id")
    )
    private List<Service> services = new ArrayList<>();
}
