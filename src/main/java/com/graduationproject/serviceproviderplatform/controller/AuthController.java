package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin
@RestController
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private AdminRepository adminRepository;
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private CompanyRepository companyRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    public AuthController(AdminRepository adminRepository,
                          UserRepository userRepository,
                          CustomerRepository customerRepository,
                          EmployeeRepository employeeRepository,
                          CompanyRepository companyRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody UserLoginDTO userDTO, BindingResult bindingResult) {
        System.out.println(userDTO);
        System.out.println("Login request!");
        if (bindingResult.hasErrors()) {
            System.out.println("Bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Admin> admin = adminRepository.findByEmail(userDTO.getEmail());
        Optional<Customer> customer = customerRepository.findByEmail(userDTO.getEmail());
        Optional<Employee> employee = employeeRepository.findByEmail(userDTO.getEmail());
        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());

        if (admin.isPresent() && checkPassword(user.get().getPassword(), userDTO.getPassword())) {
            admin.get().setLastLogin(LocalDateTime.now());
            admin.get().setConfirmPassword(admin.get().getPassword());
            adminRepository.save(admin.get());
            System.out.println("Successful - Admin for company with id: " + admin.get().getCompany().getId());
            return ResponseEntity.ok(new UserDTO(admin.get()));
        } else if (customer.isPresent() && checkPassword(customer.get().getPassword(), userDTO.getPassword())) {
            customer.get().setLastLogin(LocalDateTime.now());
            customer.get().setConfirmPassword(customer.get().getPassword());
            customerRepository.save(customer.get());
            System.out.println("Successful - customer");
            return ResponseEntity.ok(new UserDTO(customer.get()));
        } else if (employee.isPresent() && checkPassword(employee.get().getPassword(), userDTO.getPassword())) {
            employee.get().setLastLogin(LocalDateTime.now());
            employee.get().setConfirmPassword(employee.get().getPassword());
            employeeRepository.save(employee.get());
            System.out.println("Successful - employee");
            return ResponseEntity.ok(new UserDTO(employee.get()));
        } else if(user.isPresent() && checkPassword(user.get().getPassword(), userDTO.getPassword())) {
            user.get().setLastLogin(LocalDateTime.now());
            user.get().setConfirmPassword(user.get().getPassword());
            userRepository.save(user.get());
            System.out.println("Successful - ADMIN"); // Then this will be the main admin
            return ResponseEntity.ok(new UserDTO(user.get()));
        } else {
            System.out.println("Wrong email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register/company")
    public ResponseEntity<String> register(@Valid @RequestBody CompanyDTO companyDTO, BindingResult bindingResult) {
        System.out.println("Register Company request!");
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            System.out.println("Bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(userRepository.existsByEmail(companyDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use!");
        }
        if(companyRepository.existsByName(companyDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Name is already in use!");
        }

        String secret = passwordEncoder.encode(companyDTO.getPassword());
        Role adminRole = roleRepository.findByName("ROLE_CADMIN");


        Admin admin = new Admin(companyDTO.getName(), companyDTO.getEmail(), secret, true, null);
        admin.addRole(adminRole);
        admin.setConfirmPassword(secret);
        admin = adminRepository.save(admin);

        Company company = new Company(companyDTO);
        company = companyRepository.save(company);
        admin.setCompany(company);
        adminRepository.save(admin);

        System.out.println(company);
        return ResponseEntity.ok("Company registered successfully - id = " + company.getId());
    }

    private boolean checkPassword(String storedPassword, String enteredPassword) {
        System.out.println("Stored password: " + storedPassword);
        System.out.println("Entered Password: " + enteredPassword);
        return passwordEncoder.matches(enteredPassword, storedPassword);
    }


    @PostMapping("/register/customer")
    public ResponseEntity<String> register(@Valid @RequestBody Customer customer, BindingResult bindingResult) {
        System.out.println("Register customer request!");
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(userRepository.existsByEmail(customer.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use!");
        }
        if(userRepository.existsByFullName(customer.getFullName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Name is already in use!");
        }
        if(!customer.getPassword().equals(customer.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and confirm password don't match");
        }

        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER");
        customer.addRole(customerRole);
        String secret = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(secret);
        customer.setConfirmPassword(secret);
        customer.setEnabled(true);
        customer = customerRepository.save(customer);
        System.out.println(customer);
        return ResponseEntity.ok("Customer registered successfully - id = " + customer.getId());
    }

    @PostMapping("/register/employee")
    public ResponseEntity<String> registerNewUser(@Valid @RequestBody Employee employee, BindingResult bindingResult) {
        System.out.println("Register employee request!");
        System.out.println(employee);
        if (bindingResult.hasErrors()) {
            //System.out.println(bindingResult.getAllErrors());
            System.out.println("Bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(userRepository.existsByEmail(employee.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use!");
        }
        if(userRepository.existsByFullName(employee.getFullName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Name is already in use!");
        }
        Company newCompany = null;
        if(employee.getCompanyName() != null && !employee.getCompanyName().equals("")) {
            Optional<Company> company = companyRepository.findByName(employee.getCompanyName());
            if(company.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no company with the name entered");
            }
            newCompany = company.get();
        }

        employee.setCompany(newCompany);
        Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE");
        employee.addRole(employeeRole);
        String secret = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(secret);
        employee.setConfirmPassword(secret);
        employee.setEnabled(newCompany == null ? true : false);
        employee = employeeRepository.save(employee);
        System.out.println(employee);

        // We also need to send a notification to the company to ensure that this employee belong to it to make him available.
        return ResponseEntity.ok("Employee registered successfully - id = " + employee.getId());
    }
}

