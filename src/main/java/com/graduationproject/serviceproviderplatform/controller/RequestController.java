package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.Category;
import com.graduationproject.serviceproviderplatform.model.CategoryDTO;
import com.graduationproject.serviceproviderplatform.model.Request;
import com.graduationproject.serviceproviderplatform.model.RequestDTO;
import com.graduationproject.serviceproviderplatform.repository.CustomerRepository;
import com.graduationproject.serviceproviderplatform.repository.EmployeeRepository;
import com.graduationproject.serviceproviderplatform.repository.RequestRepository;
import com.graduationproject.serviceproviderplatform.repository.ServiceRepository;
import com.graduationproject.serviceproviderplatform.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/requests")
@CrossOrigin
public class RequestController {
    private RequestRepository requestRepository;
    private RequestService requestService;
    private ServiceRepository serviceRepository;
    private EmployeeRepository employeeRepository;
    private CustomerRepository customerRepository;

    public RequestController(RequestRepository requestRepository, RequestService requestService, ServiceRepository serviceRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository) {
        this.requestRepository = requestRepository;
        this.requestService = requestService;
        this.serviceRepository = serviceRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long id) {
        Optional<Request> request = requestRepository.findById(id);
        if (request.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(request.get());
    }

    @PostMapping
    public ResponseEntity<String> createRequest(@Valid @RequestBody RequestDTO requestDTO, BindingResult bindingResult) {
        System.out.println(requestDTO);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if (requestDTO.getServiceId() == null || requestDTO.getCustomerId() == null || requestDTO.getAppointment() == null || requestDTO.getChoices() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        Request request = new Request(requestDTO);  // service, employee, customer
        request.setStatus("suspended");

        if (serviceRepository.existsById(requestDTO.getServiceId())) {
            request.setService(serviceRepository.findById(requestDTO.getServiceId()).get());
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + requestDTO.getServiceId());

        if (customerRepository.existsById(requestDTO.getCustomerId())) {
            request.setCustomer(customerRepository.findById(requestDTO.getCustomerId()).get());
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no customer with id = " + requestDTO.getCustomerId());

        if (requestDTO.getEmployeeId() != null) {
            if (employeeRepository.existsById(requestDTO.getEmployeeId())) {
                request.setEmployee(employeeRepository.findById(requestDTO.getEmployeeId()).get());
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no employee with id = " + requestDTO.getEmployeeId());
        }

        request = requestRepository.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Request created successfully with id = " + request.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody RequestDTO requestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        Optional<Request> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no request with id = " + id);
        }

        Request updatedRequest = optionalRequest.get();
        updatedRequest.setStatus(requestDTO.getStatus());
        updatedRequest.setChoices(requestDTO.getChoices()); // Should we allow the customer to change them?

        // Update the appointment
        if(updatedRequest.getAppointment() != null && requestDTO.getAppointment() != null) {
            updatedRequest.getAppointment().setStartDate(requestDTO.getAppointment().getStartDate());
            updatedRequest.getAppointment().setEndDate(requestDTO.getAppointment().getEndDate());
            updatedRequest.getAppointment().setStartTime(requestDTO.getAppointment().getStartTime());
            updatedRequest.getAppointment().setEndTime(requestDTO.getAppointment().getEndTime());
            // The address can't be changed
        }

        // Update the employee
        if (requestDTO.getEmployeeId() == null) {
            updatedRequest.setEmployee(null);
        } else if (employeeRepository.existsById(requestDTO.getEmployeeId())) {
            updatedRequest.setEmployee(employeeRepository.findById(requestDTO.getEmployeeId()).get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no employee with id = " + requestDTO.getEmployeeId());
        }

        // Update feedback
        if(requestDTO.getFeedback() == null) {
            updatedRequest.setFeedback(null);
        } else if(updatedRequest.getFeedback() == null) {
            updatedRequest.setFeedback(requestDTO.getFeedback());
        } else {
            updatedRequest.getFeedback().setRating(requestDTO.getFeedback().getRating());
            updatedRequest.getFeedback().setDescription(requestDTO.getFeedback().getDescription());
            updatedRequest.getFeedback().setDate(requestDTO.getFeedback().getDate());
            updatedRequest.getFeedback().setCustomer(updatedRequest.getCustomer());
            updatedRequest.getFeedback().setEmployee(updatedRequest.getEmployee());
        }

        requestRepository.save(updatedRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Request updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable Long id) {
        if (!requestRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no request with id = " + id);
        }
        Request request = requestRepository.findById(id).get();
        requestService.delete(request);
        return ResponseEntity.status(HttpStatus.OK).body("Request deleted successfully");
    }
}
