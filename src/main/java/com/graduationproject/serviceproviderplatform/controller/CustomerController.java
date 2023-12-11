package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.CustomerRepository;
import com.graduationproject.serviceproviderplatform.repository.UserRepository;
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

    public CustomerController(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllUsers() {
        List<Customer> customers = customerRepository.findAll();
        System.out.println("Customers: " + customers);
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getUser(@PathVariable Long id) {
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
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO customerDTO, BindingResult bindingResult) {
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
        updatedCustomer.setLocation(customerDTO.getLocation());

        customerRepository.save(updatedCustomer);
        return ResponseEntity.status(HttpStatus.OK).body("Customer updated successfully");
    }

    //@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if(customerRepository.findById(id).isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no customer with id = " + id);
        customerRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Customer deleted successfully");
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<List<Request>> getUserRequests(@PathVariable Long id) {
        if(!customerRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Customer customer = customerRepository.findById(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(customer.getRequests());
    }

    @GetMapping("/{id}/incomplete-requests")
    public ResponseEntity<List<Request>> getUserIncompleteRequests(@PathVariable Long id) {
        if(!customerRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Customer customer = customerRepository.findById(id).get();
        List<Request> requests = customer.getRequests();
        List<Request> incompleteRequests = new ArrayList<>();
//        if(requests == null) {
//            System.out.println("There is no in-complete request");
//            return ResponseEntity.status(HttpStatus.OK).body(null);
//        };
        for(Request request: requests) {
            if(request.getStatus().equals("inComplete")) {
                incompleteRequests.add(request);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(incompleteRequests);
    }
}
