package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.Admin;
import com.graduationproject.serviceproviderplatform.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);

    void deleteByFullName(String name);

    Optional<Admin> findByCompany(Company company);
}
