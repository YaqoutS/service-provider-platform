package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
