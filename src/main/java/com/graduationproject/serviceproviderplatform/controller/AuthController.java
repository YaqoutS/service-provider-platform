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
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private CompanyRepository companyRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    public AuthController(UserRepository userRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository, CompanyRepository companyRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
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

        Optional<Company> company = companyRepository.findByEmail(userDTO.getEmail());
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

    private boolean checkPassword(String storedPassword, String enteredPassword) {
        System.out.println("Stored password: " + storedPassword);
        System.out.println("Entered Password: " + enteredPassword);
        return passwordEncoder.matches(enteredPassword, storedPassword);
    }

    @PostMapping("/register/customer")
    public ResponseEntity<String> register(@Valid @RequestBody User user, BindingResult bindingResult) {
        System.out.println("Register user request!");
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            System.out.println("Bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
        if(userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use!");
        }
        if(userRepository.existsByFullName(user.getFullName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Name is already in use!");
        }
        if(!user.getPassword().equals(user.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and confirm password don't match");
        }

        Role userRole = roleRepository.findByName("ROLE_CUSTOMER");
        user.addRole(userRole);
        String secret = passwordEncoder.encode(user.getPassword());
        user.setPassword(secret);
        user.setConfirmPassword(secret);
        User newUser = userRepository.save(user);
        System.out.println(newUser);
        return ResponseEntity.ok("User registered successfully!");
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

