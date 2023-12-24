package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.Employee;
import com.graduationproject.serviceproviderplatform.model.Request;
import com.graduationproject.serviceproviderplatform.model.Service;
import com.graduationproject.serviceproviderplatform.model.ServiceFeedback;
import com.graduationproject.serviceproviderplatform.repository.EmployeeRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private RequestService requestService; // When deleting an employee, all related requests will be deleted and the feedback for each request will be deleted with it
    private FeedbackService feedbackService;

    public EmployeeService(EmployeeRepository employeeRepository, RequestService requestService, FeedbackService feedbackService) {
        this.employeeRepository = employeeRepository;
        this.requestService = requestService;
        this.feedbackService = feedbackService;
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
        employeeRepository.delete(employee);
    }
}
