package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.CustomerRepository;
import com.graduationproject.serviceproviderplatform.repository.UserRepository;
import com.graduationproject.serviceproviderplatform.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
@CrossOrigin
public class CustomerController {
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private CustomerService customerService;

    public CustomerController(UserRepository userRepository, CustomerRepository customerRepository, CustomerService customerService) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        System.out.println("Customers: " + customers);
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        System.out.println("Inside get customer");
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()) {
            System.out.println("There is no customer with id = " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        //System.out.println("User: " + user);
        return ResponseEntity.status(HttpStatus.OK).body(customer.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @Valid @RequestBody UserDTO customerDTO, BindingResult bindingResult) {
        System.out.println("Inside update Customer");
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(id != customerDTO.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id in the Url is different from the one in the body");
        }
        if(!customerRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no customer with id = " + id);
        }
        Customer updatedCustomer = customerRepository.findById(id).get();
        updatedCustomer.setConfirmPassword(updatedCustomer.getPassword());
        System.out.println("Customer: " + customerDTO);
        System.out.println("Updated customer: " + updatedCustomer);

        if(userRepository.existsByFullName(customerDTO.getFullName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This name is used by another User!");
        }
        updatedCustomer.setFullName(customerDTO.getFullName());
        updatedCustomer.setDateOfBirth(customerDTO.getDateOfBirth());
        updatedCustomer.setImage(customerDTO.getImage());
        updatedCustomer.setAddress(customerDTO.getAddress());

        customerRepository.save(updatedCustomer);
        return ResponseEntity.status(HttpStatus.OK).body("Customer updated successfully");
    }

    //@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no customer with id = " + id);
        }
        customerService.delete(customer.get());
        return ResponseEntity.status(HttpStatus.OK).body("Customer deleted successfully");
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<List<Request>> getCustomerRequests(@PathVariable Long id) {
        if(!customerRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Customer customer = customerRepository.findById(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(customer.getRequests());
    }

    @GetMapping("/{id}/requests/{status}")
    public ResponseEntity<List<Request>> getCustomerSpecificRequests(@PathVariable Long id, @PathVariable String status) {
        if(!customerRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Customer customer = customerRepository.findById(id).get();
        List<Request> requests = customer.getRequests();
        List<Request> incompleteRequests = new ArrayList<>();
        for(Request request: requests) {
            if(request.getStatus().equals(status)) {
                incompleteRequests.add(request);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(incompleteRequests);
    }

    //    @GetMapping("/{id}/feedbacks")
//    public ResponseEntity<List<ServiceFeedback>> getCustomerFeedbacks(@PathVariable Long id) {
//        if(!customerRepository.existsById(id)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        List<ServiceFeedback> feedbacks = customerRepository.findById(id).get().getFeedbacks();
//        return ResponseEntity.ok(feedbacks);
//    }
}
