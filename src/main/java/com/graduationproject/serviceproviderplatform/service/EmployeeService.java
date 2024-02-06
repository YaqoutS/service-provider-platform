package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.EmployeeRepository;
import com.graduationproject.serviceproviderplatform.repository.RequestRepository;
import com.graduationproject.serviceproviderplatform.repository.ServiceFeedbackRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private RequestRepository requestRepository;
    private ServiceFeedbackRepository feedbackRepository;
    private RequestService requestService;
    private FeedbackService feedbackService;
    private SupplyService supplyService;

    public EmployeeService(EmployeeRepository employeeRepository, RequestRepository requestRepository, ServiceFeedbackRepository feedbackRepository, RequestService requestService, FeedbackService feedbackService, SupplyService supplyService) {
        this.employeeRepository = employeeRepository;
        this.requestRepository = requestRepository;
        this.feedbackRepository = feedbackRepository;
        this.requestService = requestService;
        this.feedbackService = feedbackService;
        this.supplyService = supplyService;
    }

    @Transactional
    public void delete(Employee employee) {
        for (Service service : employee.getServices()) {
            service.removeEmployee(employee);
        }
        for (ServiceFeedback feedback : employee.getFeedbacks()) {
            feedback.setEmployee(null);
            feedbackRepository.save(feedback);
        }
        for (Request request : employee.getRequests()) {
            request.setEmployee(null);
            requestRepository.save(request);
        }
        for (Supply supply : employee.getSupplies()) {
            supplyService.delete(supply);
        }
        employeeRepository.delete(employee);
    }
}
