package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer extends User {
    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @ToString.Exclude
    private List<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @ToString.Exclude
    private List<ServiceFeedback> feedbacks = new ArrayList<>();

    public Customer(String name, String email, String password, boolean enabled) {
        super(name, email, password, enabled);
    }

    public Customer(String name, String email, String password, String confirmPassword, boolean enabled, Address address, String phone) {
        super(name, email, password, confirmPassword, enabled, address, phone);
    }
}
