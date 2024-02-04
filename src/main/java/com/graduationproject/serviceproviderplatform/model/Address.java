package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String line1;

    private String line2;

    private String postalCode;

    private Double latitude;

    private Double longitude;

    public Address(String city, String line1, String line2, String postalCode) {
        this.city = city;
        this.line1 = line1;
        this.line2 = line2;
        this.postalCode = postalCode;
    }
}
