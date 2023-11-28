package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
