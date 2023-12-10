package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NonNull
    private String fullName;

    private int age;

    private String location;

    @NonNull
    private String password;

//    @NonNull
//    private boolean enabled;

    private Image image;
}
