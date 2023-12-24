package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.Employee;
import com.graduationproject.serviceproviderplatform.model.Request;
import com.graduationproject.serviceproviderplatform.model.Service;
import com.graduationproject.serviceproviderplatform.model.ServiceOption;
import com.graduationproject.serviceproviderplatform.repository.ServiceRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class ServiceService {
    private ServiceRepository serviceRepository;
    private OptionService optionService;
    private RequestService requestService;

    public ServiceService(ServiceRepository serviceRepository, OptionService optionService, RequestService requestService) {
        this.serviceRepository = serviceRepository;
        this.optionService = optionService;
        this.requestService = requestService;
    }

    @Transactional
    public void delete(Service service) {
        for (Employee employee : service.getEmployees()) {
            employee.removeService(service);
        }
        for (ServiceOption option: service.getServiceOptions()) {
            optionService.delete(option);
        }
        for (Request request : service.getRequests()) {
            requestService.delete(request);
        }
        serviceRepository.delete(service);
    }
}
