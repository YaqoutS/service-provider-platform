package com.graduationproject.serviceproviderplatform;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ServiceProviderPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceProviderPlatformApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(CategoryRepository categoryRepository,
                                      UserRepository userRepository,
                                      EmployeeRepository employeeRepository,
                                      RoleRepository roleRepository,
                                      CustomerRepository customerRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("Inside the command line runner");

            String secret = passwordEncoder.encode("123456");

            Role administratorRole = new Role("ROLE_ ADMIN"); // This is our main admin
            roleRepository.save(administratorRole);
            Role adminRole = new Role("ROLE_CADMIN");
            roleRepository.save(adminRole);
            Role customerRole = new Role("ROLE_CUSTOMER");
            roleRepository.save(customerRole);
            Role employeeRole = new Role("ROLE_EMPLOYEE");
            roleRepository.save(employeeRole);

            User admin = new User("admin@gmail.com", "Admin Admin", secret, true);
            admin.addRole(administratorRole);
            admin.setConfirmPassword(secret);
            admin.setDateOfBirth(LocalDate.of(2001, 11, 18));
            userRepository.save(admin);

            Customer customer = new Customer("customer@gmail.com", "Customer Customer", secret, true);
            customer.addRole(customerRole);
            customer.setConfirmPassword(secret);
            customerRepository.save(customer);

            Employee employee = new Employee("employee@gmail.com", "Employee Employee", secret, true, true);
            employee.addRole(employeeRole);
            employee.setConfirmPassword(secret);
            employeeRepository.save(employee);

            System.out.println("Users and roles added successfully");

            // Adding categories
            Category category1 = new Category("Category 1");
            Category category2 = new Category("Category 2");
            Category category3 = new Category("Category 3");
            categoryRepository.save(category1);
            categoryRepository.save(category2);
            categoryRepository.save(category3);
            System.out.println("Categories added successfully");

        };
    }
}
