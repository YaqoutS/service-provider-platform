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
                                      AdminRepository adminRepository,
                                      EmployeeRepository employeeRepository,
                                      CustomerRepository customerRepository,
                                      RoleRepository roleRepository,
                                      CompanyRepository companyRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("Inside the command line runner");

            String secret = passwordEncoder.encode("123456");

            Role administratorRole = new Role("ROLE_ADMIN"); // This is our main admin
            roleRepository.save(administratorRole);
            Role companyAdminRole = new Role("ROLE_CADMIN");
            roleRepository.save(companyAdminRole);
            Role customerRole = new Role("ROLE_CUSTOMER");
            roleRepository.save(customerRole);
            Role employeeRole = new Role("ROLE_EMPLOYEE");
            roleRepository.save(employeeRole);

            User mainAdmin = new User("Main Admin", "admin@gmail.com", secret, true);
            mainAdmin.addRole(administratorRole);
            mainAdmin.setConfirmPassword(secret);
            mainAdmin.setDateOfBirth(LocalDate.of(2001, 11, 18));
            userRepository.save(mainAdmin);

            Company company = new Company("Company 1");
            Company newCompany = companyRepository.save(company);

            Admin admin = new Admin("company 1", "company1@gmail.com", secret, true, newCompany);
            admin.addRole(companyAdminRole);
            admin.setConfirmPassword(secret);
            admin.setCompany(newCompany);
            Admin newAdmin = adminRepository.save(admin);
//            newCompany.setAdmin(newAdmin);
            companyRepository.save(newCompany);
            System.out.println(newCompany);

            Customer customer = new Customer("Customer 1", "customer@gmail.com", secret, true);
            customer.addRole(customerRole);
            customer.setConfirmPassword(secret);
            customerRepository.save(customer);

            Employee employee = new Employee("Employee 1", "employee@gmail.com", secret, true);
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
