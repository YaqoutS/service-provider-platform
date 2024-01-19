package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Supply extends ServiceOption {
    @ManyToOne
    @NonNull
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;
    @ManyToMany(mappedBy = "supplies")
    @ToString.Exclude
    @JsonIgnore
    private Set<Service> services = new HashSet<>();

    public void addService(Service service) {
        services.add(service);
    }
    public void removeService(Service service) {
        services.remove(service);
    }
}
