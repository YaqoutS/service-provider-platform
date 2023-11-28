package com.graduationproject.serviceproviderplatform.model.validator;

import com.graduationproject.serviceproviderplatform.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordsMatchValidator implements ConstraintValidator<PasswordMatch, User> {

    @Override
    public void initialize(PasswordMatch passwordsMatch){
    }

    public boolean isValid(User user, ConstraintValidatorContext context) {
        return user.getPassword().equals(user.getConfirmPassword());
    }

}
