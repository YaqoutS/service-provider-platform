package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.ServiceOption;
import com.graduationproject.serviceproviderplatform.repository.ServiceOptionRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class OptionService {
    private ServiceOptionRepository serviceOptionRepository;

    public OptionService(ServiceOptionRepository serviceOptionRepository) {
        this.serviceOptionRepository = serviceOptionRepository;
    }

    @Transactional
    public void delete(ServiceOption option) {
        serviceOptionRepository.delete(option);
    }
}
