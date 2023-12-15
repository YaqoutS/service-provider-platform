package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    @JoinColumn(name = "company_id", unique = true)
    private Company company;
}
