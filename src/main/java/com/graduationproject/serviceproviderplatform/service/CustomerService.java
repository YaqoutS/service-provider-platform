package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.Customer;
import com.graduationproject.serviceproviderplatform.model.Employee;
import com.graduationproject.serviceproviderplatform.model.Request;
import com.graduationproject.serviceproviderplatform.model.ServiceFeedback;
import com.graduationproject.serviceproviderplatform.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private RequestService requestService;
    private FeedbackService feedbackService;

    public CustomerService(CustomerRepository customerRepository, RequestService requestService, FeedbackService feedbackService) {
        this.customerRepository = customerRepository;
        this.requestService = requestService;
        this.feedbackService = feedbackService;
    }

    @Transactional
    public void delete(Customer customer) {
        for (Request request : customer.getRequests()) {
            requestService.delete(request);
        }
        for (ServiceFeedback feedback : customer.getFeedbacks()) {
            feedbackService.delete(feedback);
        }
        customerRepository.delete(customer);
    }
}
