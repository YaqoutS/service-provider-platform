package com.graduationproject.serviceproviderplatform;

import com.graduationproject.serviceproviderplatform.model.*;
import com.graduationproject.serviceproviderplatform.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ServiceProviderPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceProviderPlatformApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner initData(CategoryRepository categoryRepository,
//                                      UserRepository userRepository,
//                                      AdminRepository adminRepository,
//                                      EmployeeRepository employeeRepository,
//                                      CustomerRepository customerRepository,
//                                      RoleRepository roleRepository,
//                                      CompanyRepository companyRepository,
//                                      PasswordEncoder passwordEncoder) {
//        return args -> {
//            System.out.println("Inside the command line runner");
//
//            String secret = passwordEncoder.encode("123456");
//
//            // Add roles
//            Role administratorRole = new Role("ROLE_ADMIN"); // This is our main admin
//            administratorRole = roleRepository.save(administratorRole);
//            Role companyAdminRole = new Role("ROLE_CADMIN");
//            companyAdminRole = roleRepository.save(companyAdminRole);
//            Role customerRole = new Role("ROLE_CUSTOMER");
//            customerRole = roleRepository.save(customerRole);
//            Role employeeRole = new Role("ROLE_EMPLOYEE");
//            employeeRole = roleRepository.save(employeeRole);
//
//            // Add the main admin
//            User mainAdmin = new User("Main Admin", "admin@gmail.com", secret, true);
//            mainAdmin.addRole(administratorRole);
//            mainAdmin.setConfirmPassword(secret);
//            mainAdmin.setDateOfBirth(LocalDate.of(2001, 11, 18));
//            userRepository.save(mainAdmin);
//
//            // Create addresses
//            Address address1 = new Address("Ramallah", "Main Street", "Building 123", "112233");
//            Address address2 = new Address("Bethlehem", "Oak Avenue", "Apartment 456", "334455");
//            Address address3 = new Address("Hebron", "Palm Street", "Suite 789", "556677");
//            Address address4 = new Address("Jericho", "Maple Lane", "House 101", "778899");
//            Address address5 = new Address("Nablus", "Cedar Road", "Flat 202", "990011");
//            Address address6 = new Address("Nablus", "Rafedia Road", "Flat 180", "990311");
//            Address address7 = new Address("Ramallah", "Tech Park Street", "Building A", "112233");
//            Address address8 = new Address("Bethlehem", "Business District", "Tower B", "334455");
//            Address address9 = new Address("Hebron", "Commercial Area", "Plaza 1", "556677");
//            Address address10 = new Address("Jericho", "Innovation Street", "Tech Hub", "778899");
//            Address address11 = new Address("Nablus", "Science Avenue", "Research Center", "990011");
//            Address address12 = new Address("Ramallah", "Tech Park Street", "Building C", "334455");
//            Address address13 = new Address("Bethlehem", "Business District", "Tower D", "556677");
//            Address address14 = new Address("Hebron", "Commercial Area", "Plaza 3", "778899");
//            Address address15 = new Address("Jericho", "Innovation Street", "Tech Hub", "990011");
//            Address address16 = new Address("Nablus", "Science Avenue", "Research Center", "001122");
//            Address address17 = new Address("Nablus", "Science Avenue", "Flat 202", "001122");
//
//
//            // Add categories
//            Category category1 = new Category("Electricity");
//            Category category2 = new Category("Construction and Contracting");
//            Category category3 = new Category("Automotive Maintenance and Repair");
//            Category category4 = new Category("Mobile and Tablet Repair");
//            Category category5 = new Category("Furniture Moving");
//            Category category6 = new Category("Water Transportation");
//            Category category7 = new Category("Fuel Transportation");
//            Category category8 = new Category("Computer System Maintenance");
//            Category category9 = new Category("Cleaning");
//            Category category10 = new Category("Painting and Coating");
//            Category category11 = new Category("Plumbing");
//            Category category12 = new Category("Blacksmithing and Welding");
//            Category category13 = new Category("Tile and Parquet Installation");
//            Category category14 = new Category("Carpentry and Furniture Restoration");
//            Category category15 = new Category("Kitchen Installation and Maintenance");
//            Category category16 = new Category("Bathroom Installation and Maintenance");
//            Category category17 = new Category("Gardening");
//            Category category18 = new Category("Door and Window Repair");
//            Category category19 = new Category("General Maintenance");
//            Category category20 = new Category("Home Care");
//
//            categoryRepository.save(category1);
//            categoryRepository.save(category2);
//            categoryRepository.save(category3);
//            categoryRepository.save(category4);
//            categoryRepository.save(category5);
//            categoryRepository.save(category6);
//            categoryRepository.save(category7);
//            categoryRepository.save(category8);
//            categoryRepository.save(category9);
//            categoryRepository.save(category10);
//            categoryRepository.save(category11);
//            categoryRepository.save(category12);
//            categoryRepository.save(category13);
//            categoryRepository.save(category14);
//            categoryRepository.save(category15);
//            categoryRepository.save(category16);
//            categoryRepository.save(category17);
//            categoryRepository.save(category18);
//            categoryRepository.save(category19);
//            categoryRepository.save(category20);
//
//            // Add services
//
//
//            // Add companies
//
//            Set<DayOfWeek> workDays = new HashSet<>();
//            workDays.add(DayOfWeek.MONDAY);
//            workDays.add(DayOfWeek.WEDNESDAY);
//
//            Company company1 = new Company("SparkleClean Services", "Cleaning", "Exceptional cleaning solutions for a sparkling environment. We transform spaces into pristine havens.", LocalTime.of(8, 0), LocalTime.of(16, 0));
//            company1.setPhone("1234567890");
//            company1.setAddress(address6);
//            company1.setWorkDays(workDays);
//
//            Company company2 = new Company("ArtisanCraft Creations", "Carpentry", "Crafting excellence in every piece. Our skilled carpenters bring your visions to life with precision and creativity.", LocalTime.of(8, 0), LocalTime.of(16, 0));
//            company2.setPhone("9876543210");
//            company2.setAddress(address7);
//
//            Company company3 = new Company("PowerFlow Systems", "Electrical Installations", "Empowering your spaces with top-notch electrical solutions. We ensure safety and efficiency in every installation.", LocalTime.of(8, 0), LocalTime.of(16, 0));
//            company3.setPhone("1112223333");
//            company3.setAddress(address8);
//
//            Company company4 = new Company("RiseTech Elevators", "Elevators", "Raising standards with cutting-edge elevator solutions. Elevate your buildings with our reliable and innovative technology.", LocalTime.of(8, 0), LocalTime.of(16, 0));
//            company4.setPhone("5556667777");
//            company4.setAddress(address9);
//
//            Company company5 = new Company("TechPro Experts", "Home Automation and Smart Technology", "Your go-to solution for home automation and smart technology. TechPro Experts offers expert services in installing and maintaining smart home devices, security systems, and audiovisual setups. Elevate your living experience with our cutting-edge solutions", LocalTime.of(8, 0), LocalTime.of(16, 0));
//            company5.setPhone("9990001111");
//            company5.setAddress(address10);
//
//            company1 = companyRepository.save(company1);
//            company2 = companyRepository.save(company2);
//            company3 = companyRepository.save(company3);
//            company4 = companyRepository.save(company4);
//            company5 = companyRepository.save(company5);
//
//            // Add companies' admins
//            Admin admin1 = new Admin("SparkleClean Services", "admin1@gmail.com", secret, true, company1);
//            admin1.addRole(companyAdminRole);
//            admin1.setConfirmPassword(secret);
//            adminRepository.save(admin1);
//
//            Admin admin2 = new Admin("ArtisanCraft Creations", "admin2@gmail.com", secret, true, company2);
//            admin2.addRole(companyAdminRole);
//            admin2.setConfirmPassword(secret);
//            adminRepository.save(admin2);
//
//            Admin admin3 = new Admin("PowerFlow Systems", "admin3@gmail.com", secret, true, company3);
//            admin3.addRole(companyAdminRole);
//            admin3.setConfirmPassword(secret);
//            adminRepository.save(admin3);
//
//            Admin admin4 = new Admin("RiseTech Elevators", "admin4@gmail.com", secret, true, company4);
//            admin4.addRole(companyAdminRole);
//            admin4.setConfirmPassword(secret);
//            adminRepository.save(admin4);
//
//            Admin admin5 = new Admin("TechPro Experts", "admin5@gmail.com", secret, true, company5);
//            admin5.addRole(companyAdminRole);
//            admin5.setConfirmPassword(secret);
//            adminRepository.save(admin5);
//
//            // Add customers
//            Customer custome1 = new Customer("Mark User", "user1@gmail.com", secret, secret, true, address5, "9990001112");
//            custome1.addRole(customerRole);
//            custome1.setDateOfBirth(LocalDate.of(1988, 12, 3));
//            customerRepository.save(custome1);
//
//            Customer custome2 = new Customer("Maria User", "user2@gmail.com", secret, secret, true, address16, "3334445556");
//            custome2.addRole(customerRole);
//            custome2.setDateOfBirth(LocalDate.of(1990, 6, 25));
//            customerRepository.save(custome2);
//
//            // Add employees
////          -- Employees for (SparkleClean Services)
//            Employee employee1 = new Employee("Alex Employee", "employee1@example.com", secret, secret, true, company1, 5, address1, "1112233445");
//            employee1.addRole(employeeRole);
//            employee1.setDateOfBirth(LocalDate.of(1995, 3, 10));
//            employeeRepository.save(employee1);
//
//            Employee employee2 = new Employee("Emily Employee", "employee2@example.com", secret, secret, true, company1, 7, address2, "5556677889");
//            employee2.addRole(employeeRole);
//            employee2.setDateOfBirth(LocalDate.of(1992, 8, 20));
//            employeeRepository.save(employee2);
//
////          -- Employees for  (ArtisanCraft Creations)
//            Employee employee3 = new Employee("Ethan Employee", "employee3@example.com", secret, secret, true, company2, 6, address17, "1112233446");
//            employee3.addRole(employeeRole);
//            employee3.setDateOfBirth(LocalDate.of(1991, 6, 15));
//            employeeRepository.save(employee3);
//
//            Employee employee4 = new Employee("Olivia Employee", "employee4@example.com", secret, secret, true, company2, 8, address11, "5556677890");
//            employee4.addRole(employeeRole);
//            employee4.setDateOfBirth(LocalDate.of(1993, 11, 25));
//            employeeRepository.save(employee4);
//
////          -- Employees for  (PowerFlow Systems)
//            Employee employee5 = new Employee("Daniel Employee", "employee5@example.com", secret, secret, true, company3, 7, address12, "1112233447");
//            employee5.addRole(employeeRole);
//            employee5.setDateOfBirth(LocalDate.of(1990, 4, 5));
//            employeeRepository.save(employee5);
//
//            Employee employee6 = new Employee("Sophia Employee", "employee6@example.com", secret, secret, true, company3, 9, address13, "5556677891");
//            employee6.addRole(employeeRole);
//            employee6.setDateOfBirth(LocalDate.of(1988, 9, 15));
//            employeeRepository.save(employee6);
//
////          -- Employees for  (RiseTech Elevators)
//            Employee employee7 = new Employee("Matthew Employee", "employee7@example.com", secret, secret, true, company4, 5, address14, "1112233448");
//            employee7.addRole(employeeRole);
//            employee7.setDateOfBirth(LocalDate.of(1994, 2, 20));
//            employeeRepository.save(employee7);
//
//            Employee employee8 = new Employee("Ava Employee", "employee8@example.com", secret, secret, true, company4, 8, address15, "5556677892");
//            employee8.addRole(employeeRole);
//            employee8.setDateOfBirth(LocalDate.of(1996, 7, 10));
//            employeeRepository.save(employee8);
//
////          -- Employees for (TechPro Eperts)
//            Employee employee9 = new Employee("Alex Employee", "employee9@example.com", secret, secret, true, company5, 5, address4, "1112233445");
//            employee9.addRole(employeeRole);
//            employee9.setDateOfBirth(LocalDate.of(1995, 3, 10));
//            employeeRepository.save(employee9);
//
//            Employee employee10 = new Employee("Emily Employee", "employee10@example.com", secret, secret, true, company5, 7, address3, "5556677889");
//            employee10.addRole(employeeRole);
//            employee10.setDateOfBirth(LocalDate.of(1992, 8, 20));
//            employeeRepository.save(employee10);
//
//            System.out.println("Initial data added successfully");
//
//
//        };
//    }
}
