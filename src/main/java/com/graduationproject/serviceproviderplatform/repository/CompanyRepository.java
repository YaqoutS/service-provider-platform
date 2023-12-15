package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    Optional<Object> findByName(String name);
}
