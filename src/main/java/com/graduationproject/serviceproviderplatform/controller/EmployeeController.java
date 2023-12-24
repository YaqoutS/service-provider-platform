package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.EmployeeRepository;
import com.graduationproject.serviceproviderplatform.repository.ServiceRepository;
import com.graduationproject.serviceproviderplatform.repository.UserRepository;
import com.graduationproject.serviceproviderplatform.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
@CrossOrigin
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;
    private UserRepository userRepository;
    private ServiceRepository serviceRepository;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeService employeeService, UserRepository userRepository, ServiceRepository serviceRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        System.out.println("Employees: " + employees);
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Employee employee1 = employee.get();
        employee1.setCompanyName(employee1.getCompany() == null? null : employee1.getCompany().getName());
        return ResponseEntity.status(HttpStatus.OK).body(employee1);
    }

    //@Secured({"ROLE_EMPLOYEE"})
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO, BindingResult bindingResult) {
        System.out.println("Inside update employeeDTO");
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(id != employeeDTO.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id in the Url is different from the one in the body");
        }
        if(!employeeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no employee with id = " + id);
        }
        Employee updatedEmployee = employeeRepository.findById(id).get();
        updatedEmployee.setConfirmPassword(updatedEmployee.getPassword());
        System.out.println("Employee: " + employeeDTO);
        System.out.println("Updated employee: " + updatedEmployee);

        if(userRepository.existsByFullName(employeeDTO.getFullName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This name is used by another user!");
        }
        updatedEmployee.setFullName(employeeDTO.getFullName());
        updatedEmployee.setDateOfBirth(employeeDTO.getDateOfBirth());
        updatedEmployee.setAddress(employeeDTO.getAddress());
        updatedEmployee.setImage(employeeDTO.getImage());
        updatedEmployee.setRating(employeeDTO.getRating()); //I think there is no need for this line because the rating will change with every feedback, and it has its own API
        updatedEmployee.setEnabled(employeeDTO.isEnabled());
        updatedEmployee.setYearsOfExperience(employeeDTO.getYearsOfExperience());

        employeeRepository.save(updatedEmployee);
        return ResponseEntity.status(HttpStatus.OK).body("Employee updated successfully");
    }

    //@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no employee with id = " + id);
        }
        employeeService.delete(employee.get());
        return ResponseEntity.status(HttpStatus.OK).body("Employee deleted successfully");
    }

    @PutMapping("/{employeeId}/add-service/{serviceId}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId, @PathVariable Long serviceId) {

        if(!employeeRepository.existsById(employeeId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no employee with id = " + employeeId);
        }
        if(!serviceRepository.existsById(serviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no service with id = " + serviceId);
        }

        Employee employee = employeeRepository.findById(employeeId).get();
        Service service = serviceRepository.findById(serviceId).get();

        if(employee.getServices().contains(service)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Service already exists");
        }

        employee.addService(service);
        service.addEmployee(employee);
        employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.OK).body("Service added successfully");
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<List<Request>> getEmployeeRequests(@PathVariable Long id) {
        if(!employeeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Employee employee = employeeRepository.findById(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(employee.getRequests());
    }

    @GetMapping("/{id}/incomplete-requests")
    public ResponseEntity<List<Request>> getEmployeeIncompleteRequests(@PathVariable Long id) {
        if(!employeeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Employee employee = employeeRepository.findById(id).get();
        List<Request> requests = employee.getRequests();
        List<Request> incompleteRequests = new ArrayList<>();
//        if(requests == null) {
//            return ResponseEntity.status(HttpStatus.OK).body(null);
//        }
        for(Request request: requests) {
            if(request.getStatus().equals("inCompleted")) {
                incompleteRequests.add(request);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(incompleteRequests);
    }

    @GetMapping("/{id}/feedbacks")
    public ResponseEntity<List<ServiceFeedback>> getEmployeeFeedbacks(@PathVariable Long id) {
        if(!employeeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Employee employee = employeeRepository.findById(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(employee.getFeedbacks());
    }
}
