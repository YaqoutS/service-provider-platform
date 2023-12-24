package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Admin extends User {
    @OneToOne
//    @JoinColumn(name = "company_id", unique = true)
    private Company company;

    public Admin(String name, String email, String password, boolean enabled, Company company) {
        super(name, email, password, enabled);
        this.company = company;
    }
}
