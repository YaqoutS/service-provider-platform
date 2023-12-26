package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.ServiceInput;
import com.graduationproject.serviceproviderplatform.repository.ServiceInputRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InputService {
    private ServiceInputRepository serviceInputRepository;

    public InputService(ServiceInputRepository serviceInputRepository) {
        this.serviceInputRepository = serviceInputRepository;
    }

    @Transactional
    public void delete(ServiceInput serviceInput) {
        serviceInputRepository.delete(serviceInput);
    }
}
