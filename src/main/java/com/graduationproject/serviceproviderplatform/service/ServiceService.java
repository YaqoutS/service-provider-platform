package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.ServiceRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class ServiceService {
    private ServiceRepository serviceRepository;
    private OptionService optionService;
    private InputService inputService;
    private RequestService requestService;

    public ServiceService(ServiceRepository serviceRepository, OptionService optionService, InputService inputService, RequestService requestService) {
        this.serviceRepository = serviceRepository;
        this.optionService = optionService;
        this.inputService = inputService;
        this.requestService = requestService;
    }

    @Transactional
    public void delete(Service service) {
        for (Employee employee : service.getEmployees()) {
            employee.removeService(service);
        }

        for (Supply supply : service.getSupplies()) {
            supply.removeService(service);
        }
// I think there is no need for the three following loops because I added CascadeType.ALL
//        for (ServiceOption option: service.getServiceOptions()) {
//            optionService.delete(option);
//        }
//        for(ServiceInput input: service.getServiceInputs()) {
//            inputService.delete(input);
//        }
//        for (Request request : service.getRequests()) {
//            requestService.delete(request);
//        }
        serviceRepository.delete(service);
    }
}
