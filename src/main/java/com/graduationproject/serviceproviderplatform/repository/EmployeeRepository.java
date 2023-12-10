package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.Employee;
import com.graduationproject.serviceproviderplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByFullName(String email);
    boolean existsById(Long id);
    //Optional<Employee> findByEmailAndActivationCode(String email, String activationCode);
}
