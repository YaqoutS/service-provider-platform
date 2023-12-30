package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.Service;
import com.graduationproject.serviceproviderplatform.model.ServiceOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOptionRepository extends JpaRepository<ServiceOption, Long> {
    boolean existsByIdAndService(Long optionId, Service service);
}
