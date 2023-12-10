package com.graduationproject.serviceproviderplatform.model;

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
    private List<Request> requests = new ArrayList<>();
    @OneToMany(mappedBy = "customer")
    private List<ServiceFeedback> feedbacks = new ArrayList<>();
    public Customer(String email, String name, String password, boolean enabled) {
        super(email, name, password, enabled);
    }
}
