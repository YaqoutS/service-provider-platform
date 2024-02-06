package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.Customer;
import com.graduationproject.serviceproviderplatform.model.Request;
import com.graduationproject.serviceproviderplatform.model.ServiceFeedback;
import com.graduationproject.serviceproviderplatform.repository.CustomerRepository;
import com.graduationproject.serviceproviderplatform.repository.RequestRepository;
import com.graduationproject.serviceproviderplatform.repository.ServiceFeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private RequestRepository requestRepository;
    private ServiceFeedbackRepository feedbackRepository;
    private RequestService requestService;
    private FeedbackService feedbackService;

    public CustomerService(CustomerRepository customerRepository, RequestRepository requestRepository, ServiceFeedbackRepository feedbackRepository, RequestService requestService, FeedbackService feedbackService) {
        this.customerRepository = customerRepository;
        this.requestRepository = requestRepository;
        this.feedbackRepository = feedbackRepository;
        this.requestService = requestService;
        this.feedbackService = feedbackService;
    }

    @Transactional
    public void delete(Customer customer) {
        for (ServiceFeedback feedback : customer.getFeedbacks()) {
            feedback.setCustomer(null);
            feedbackRepository.save(feedback);
        }
        for (Request request : customer.getRequests()) {
            request.setCustomer(null);
            requestRepository.save(request);
        }
        customerRepository.delete(customer);
    }
}
