package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.EmployeeRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private RequestService requestService; // When deleting an employee, all related requests will be deleted and the feedback for each request will be deleted with it
    private FeedbackService feedbackService;
    private SupplyService supplyService;

    public EmployeeService(EmployeeRepository employeeRepository, RequestService requestService, FeedbackService feedbackService, SupplyService supplyService) {
        this.employeeRepository = employeeRepository;
        this.requestService = requestService;
        this.feedbackService = feedbackService;
        this.supplyService = supplyService;
    }

    @Transactional
    public void delete(Employee employee) {
        for (Service service : employee.getServices()) {
            service.removeEmployee(employee);
        }
        for (Request request : employee.getRequests()) {
            requestService.delete(request);
        }
        for (ServiceFeedback feedback : employee.getFeedbacks()) {
            feedbackService.delete(feedback);
        }
        for (Supply supply : employee.getSupplies()) {
            supplyService.delete(supply);
        }
        employeeRepository.delete(employee);
    }
}
