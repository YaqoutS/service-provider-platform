package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.*;
import com.graduationproject.serviceproviderplatform.service.FeedbackService;
import com.graduationproject.serviceproviderplatform.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    private ServiceFeedbackRepository feedbackRepository;
    private FeedbackService feedbackService;
    private OptionChoiceRepository optionChoiceRepository;
    private InputChoiceRepository inputChoiceRepository;
    private SupplyChoiceRepository supplyChoiceRepository;
    private AddressRepository addressRepository;
    private CompanyRepository companyRepository;
    public RequestController(RequestRepository requestRepository, RequestService requestService, ServiceRepository serviceRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository, ServiceFeedbackRepository feedbackRepository, FeedbackService feedbackService, OptionChoiceRepository optionChoiceRepository, InputChoiceRepository inputChoiceRepository, SupplyChoiceRepository supplyChoiceRepository, AddressRepository addressRepository, CompanyRepository companyRepository) {
        this.requestRepository = requestRepository;
        this.requestService = requestService;
        this.serviceRepository = serviceRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.feedbackRepository = feedbackRepository;
        this.feedbackService = feedbackService;
        this.optionChoiceRepository = optionChoiceRepository;
        this.inputChoiceRepository = inputChoiceRepository;
        this.supplyChoiceRepository = supplyChoiceRepository;
        this.addressRepository = addressRepository;
        this.companyRepository = companyRepository;
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long serviceId) {

        List<Request> requests;

        if (customerId != null && status != null) {
            requests = requestRepository.findByCustomer_IdAndStatus(customerId, status);
        } else if (status != null && employeeId != null && serviceId != null) {
            requests = requestRepository.findByEmployee_IdAndStatusAndService_Id(employeeId, status,serviceId);
        }else if (status != null && employeeId != null) {
            requests = requestRepository.findByEmployee_IdAndStatus(employeeId, status);

        }
        else if ( employeeId != null && serviceId != null) {
            requests = requestRepository.findByEmployee_IdAndService_Id(employeeId, serviceId);
        }
        else if (customerId != null) {
            requests = requestRepository.findByCustomer_Id(customerId);
        }
        else if (employeeId != null) {
            requests = requestRepository.findByEmployee_Id(employeeId);
        } else if (status != null) {
            requests = requestRepository.findByStatus(status);
        } else if (serviceId != null) {
            requests = requestRepository.findByService_Id(serviceId);
        }  else {
            requests = requestRepository.findAll();
        }

        return ResponseEntity.status(HttpStatus.OK).body(requests);
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
    public ResponseEntity<Request> createRequest(@Valid @RequestBody RequestDTO requestDTO, BindingResult bindingResult) {
        System.out.println(requestDTO);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (requestDTO.getServiceId() == null || requestDTO.getCustomerId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Request request = new Request(requestDTO);  // service, employee, customer
        request.setStatus("Pending");

        if (!serviceRepository.existsById(requestDTO.getServiceId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Service service = serviceRepository.findById(requestDTO.getServiceId()).get();
        request.setService(service);

        if (!customerRepository.existsById(requestDTO.getCustomerId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        request.setCustomer(customerRepository.findById(requestDTO.getCustomerId()).get());

        if (requestDTO.getEmployeeId() != null) {
            if (!employeeRepository.existsById(requestDTO.getEmployeeId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Employee employee = employeeRepository.findById(requestDTO.getEmployeeId()).get();
            if (!service.getEmployees().contains(employee)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            request.setEmployee(employee);
        }

        request.setCreatedAt(LocalDateTime.now());
        request = requestRepository.save(request);

        for (OptionChoice choice : requestDTO.getOptionChoices()) {
            choice.setRequest(request);
            optionChoiceRepository.save(choice);
        }
        for (InputChoice choice : requestDTO.getInputChoices()) {
            choice.setRequest(request);
            inputChoiceRepository.save(choice);
        }
        for (SupplyChoice choice : requestDTO.getSupplyChoices()) {
            choice.setRequest(request);
            supplyChoiceRepository.save(choice);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> updateRequest(@PathVariable Long id, @Valid @RequestBody RequestDTO requestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        Optional<Request> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no request with id = " + id);
        }

        Request updatedRequest = optionalRequest.get();
        // Update status
        updatedRequest.setStatus(requestDTO.getStatus());

        // Update price
        updatedRequest.setPrice(requestDTO.getPrice());

        // Update the appointment
        if(updatedRequest.getAppointment() == null || requestDTO.getAppointment() == null) {
            updatedRequest.setAppointment(requestDTO.getAppointment());
        }
        else if(updatedRequest.getAppointment() != null && requestDTO.getAppointment() != null) {
            updatedRequest.getAppointment().setStartDate(requestDTO.getAppointment().getStartDate());
            updatedRequest.getAppointment().setEndDate(requestDTO.getAppointment().getEndDate());
            updatedRequest.getAppointment().setStartTime(requestDTO.getAppointment().getStartTime());
            updatedRequest.getAppointment().setEndTime(requestDTO.getAppointment().getEndTime());
          }


        // Update the employee
        if (requestDTO.getEmployeeId() == null) {
            updatedRequest.setEmployee(null);
        } else if (employeeRepository.existsById(requestDTO.getEmployeeId())) {
            updatedRequest.setEmployee(employeeRepository.findById(requestDTO.getEmployeeId()).get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no employee with id = " + requestDTO.getEmployeeId());
        }

//        // Update feedback
//        if (updatedRequest.getAppointment() == null && requestDTO.getAppointment() == null) {
//            updatedRequest.setAppointment(null);
//        } else if (updatedRequest.getFeedback() == null) {
//            updatedRequest.setFeedback(requestDTO.getFeedback());
//            updatedRequest.getFeedback().setCustomer(updatedRequest.getCustomer());
//            updatedRequest.getFeedback().setEmployee(updatedRequest.getEmployee());
//            System.out.println(1);
//        } else if(requestDTO.getFeedback() == null) {
//            System.out.println(2);
//            feedbackService.delete(updatedRequest.getFeedback());
//            updatedRequest.setFeedback(null);
//        } else {
//            System.out.println(3);
//            updatedRequest.getFeedback().setRating(requestDTO.getFeedback().getRating());
//            updatedRequest.getFeedback().setDescription(requestDTO.getFeedback().getDescription());
//            updatedRequest.getFeedback().setDate(requestDTO.getFeedback().getDate());
//        }

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

    @DeleteMapping()
    public ResponseEntity<String> deleteAllRequest() {
        requestRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("Requests deleted successfully");
    }

    // ###################################### Feedback endpoints ###################################### //

    @PostMapping("/{requestId}/feedback")
    public ResponseEntity<String> createFeedback(@PathVariable Long requestId, @Valid @RequestBody ServiceFeedback feedback, BindingResult bindingResult) {
        System.out.println(feedback);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if (!requestRepository.existsById(requestId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no request with id = " + requestId);
        }

        Request request = requestRepository.findById(requestId).get();

//        employeeRepository.findById(request.getEmployee().getId()).get().updateRating();
//        serviceRepository.findById(request.getCustomer().getId()).get().updateRating();
//        if (request.getService().getCategory().getCompany() != null) {
//            companyRepository.findById(request.getService().getCategory().getCompany().getId()).get().updateRating();
//        }

//        feedback.setEmployee(request.getEmployee());
//        feedback.setCustomer(request.getCustomer());
        feedback.setRequest(request);

        feedback = feedbackRepository.save(feedback);
        request.setFeedback(feedback);
        requestRepository.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Feedback added successfully with id = " + feedback.getId());
    }

    @PutMapping("/{requestId}/feedback/{feedbackId}")
    public ResponseEntity<String> updateFeedback(@PathVariable Long requestId, @PathVariable Long feedbackId, @Valid @RequestBody ServiceFeedback feedback, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if (!requestRepository.existsById(requestId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no request with id = " + requestId);
        }
        if (!feedbackRepository.existsById(feedbackId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no feedback with id = " + feedbackId);
        }
        ServiceFeedback updatedFeedback = feedbackRepository.findById(feedbackId).get();
        updatedFeedback.setRating(feedback.getRating());
        updatedFeedback.setDescription(feedback.getDescription());
        updatedFeedback.setDate(feedback.getDate());

        feedbackRepository.save(updatedFeedback);
        return ResponseEntity.status(HttpStatus.OK).body("Feedback updated successfully");
    }

    @DeleteMapping("/{requestId}/feedback/{feedbackId}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long requestId, @PathVariable Long feedbackId) {
        if (!requestRepository.existsById(requestId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no request with id = " + requestId);
        }
        if (!feedbackRepository.existsById(feedbackId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no feedback with id = " + feedbackId);
        }
        Request request = requestRepository.findById(requestId).get();
        ServiceFeedback feedback = feedbackRepository.findById(feedbackId).get();
        request.setFeedback(null);
        feedbackService.delete(feedback);
        return ResponseEntity.status(HttpStatus.OK).body("Feedback deleted successfully");
    }
}
