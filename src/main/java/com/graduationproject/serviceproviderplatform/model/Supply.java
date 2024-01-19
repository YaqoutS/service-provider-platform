package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Supply extends ServiceOption {
    @ManyToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;
    @ManyToMany(mappedBy = "supplies")
    private Set<Service> services = new HashSet<>();

    public void addService(Service service) {
        services.add(service);
    }
    public void removeService(Service service) {
        services.remove(service);
    }
}
