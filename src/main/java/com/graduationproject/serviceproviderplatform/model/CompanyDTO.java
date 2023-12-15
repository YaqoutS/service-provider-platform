package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompanyDTO {
    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    private String field;

    private String description;

    private String phone;

    private String location;
}
