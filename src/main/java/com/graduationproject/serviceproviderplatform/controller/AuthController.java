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
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userDTO, BindingResult bindingResult) {
        System.out.println(userDTO);
        System.out.println("Login request!");
        if (bindingResult.hasErrors()) {
            System.out.println("Bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }

        Optional<Company> company = adminRepository.findByEmail(userDTO.getEmail());
        Optional<Customer> customer = customerRepository.findByEmail(userDTO.getEmail());
        Optional<Employee> employee = employeeRepository.findByEmail(userDTO.getEmail());
        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());

        if (company.isPresent() && checkPassword(user.get().getPassword(), userDTO.getPassword())) {
            System.out.println("Successful - Admin for company with id: " + company.get().getId());
            return ResponseEntity.ok("Successful");
        } else if (customer.isPresent() && checkPassword(customer.get().getPassword(), userDTO.getPassword())) {
            System.out.println("Successful - customer");
            return ResponseEntity.ok("Successful");
        } else if (employee.isPresent() && checkPassword(employee.get().getPassword(), userDTO.getPassword())) {
            System.out.println("Successful - employee");
            return ResponseEntity.ok("Successful");
        } else if(user.isPresent() && checkPassword(user.get().getPassword(), userDTO.getPassword())) {
            System.out.println("Successful - ADMIN"); // Then this will be the main admin
            return ResponseEntity.ok("Successful");
        } else {
            System.out.println("Wrong email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong email or password");
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
        if(adminRepository.existsByEmail(companyDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use!");
        }
        if(companyRepository.existsByName(companyDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Name is already in use!");
        }
//        if(!companyDTO.getPassword().equals(companyDTO.getConfirmPassword())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and confirm password don't match");
//        }

        String secret = passwordEncoder.encode(companyDTO.getPassword());
        Role adminRole = roleRepository.findByName("ROLE_CADMIN");

        Admin admin = new Admin();
        admin.addRole(adminRole);
        admin.setEmail(companyDTO.getEmail());
        admin.setPassword(secret);
        admin.setConfirmPassword(secret);
        admin.setFullName(companyDTO.getName());
        admin.setEnabled(true);

        Company company = new Company();
        company.setAdmin(admin);
        company.setName(companyDTO.getName());
        company.setImage(companyDTO.getImage());
        company.setField(companyDTO.getField());
        company.setDescription(companyDTO.getDescription());
        company.setPhone(companyDTO.getPhone());
        company.setLocation(companyDTO.getLocation());

        admin.setCompany(company);

        Company newCompany = companyRepository.save(company);
        System.out.println(newCompany);
        return ResponseEntity.ok("Company registered successfully - ");
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
        User newUser = userRepository.save(customer);
        System.out.println(newUser);
        return ResponseEntity.ok("Customer registered successfully!");
    }

    @PostMapping("/register/employee")
    public ResponseEntity<String> registerNewUser(@Valid @RequestBody Employee employee, BindingResult bindingResult) {
        System.out.println("Register employee request!");
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

        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        employee.addRole(userRole);
        String secret = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(secret);
        employee.setConfirmPassword(secret);
        Employee newEmployee = employeeRepository.save(employee);
        System.out.println(newEmployee);
        return ResponseEntity.ok("Employee registered successfully!");
    }

//    @GetMapping("/activate/{email}/{activationCode}")
//    public String activate(@PathVariable String email, @PathVariable String activationCode) {
//        Optional<User> user = userRepository.findByEmailAndActivationCode(email,activationCode);
//        if( user.isPresent() ){
//            User newUser = user.get();
//            newUser.setEnabled(true);
//            newUser.setConfirmPassword(newUser.getPassword());
//            userRepository.save(newUser);
////            userRepository.sendWelcomeEmail(newUser);
//            return "auth/activated";
//        }
//        return "redirect:/";
//    }

}

