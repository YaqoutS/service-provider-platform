package com.graduationproject.serviceproviderplatform.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserLoginDTO {
    @NonNull @NotEmpty @Email
    private String email;
    @NonNull @NotEmpty
    private String password;
}
