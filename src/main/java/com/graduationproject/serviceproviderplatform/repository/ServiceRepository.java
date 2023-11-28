package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
