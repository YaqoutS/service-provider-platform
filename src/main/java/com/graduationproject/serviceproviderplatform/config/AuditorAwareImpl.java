package com.graduationproject.serviceproviderplatform.config;

import com.graduationproject.serviceproviderplatform.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() { // get the logged-in user
        if(SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return Optional.of("admin@gmail.com"); // in the development, we add links but there is not any logged-in user
        } else {
            return Optional.of(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName());
        }
    }
}
