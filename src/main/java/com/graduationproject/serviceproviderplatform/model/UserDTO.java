package com.graduationproject.serviceproviderplatform.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NonNull
    private String fullName;

    @NonNull
    private String email;

//    private CompanyDTO company;

    private LocalDate dateOfBirth;

    private Address address;

    @NonNull
    private String password;

    private String role;

//    @NonNull
    private boolean enabled;

    private Image image;
    private Company company;

    public UserDTO(Admin admin) {
        if(admin != null) {
            this.id = admin.getId();
            this.fullName = admin.getFullName();
            this.company = admin.getCompany();
            this.dateOfBirth = admin.getDateOfBirth();
            this.address = admin.getAddress();
//        this.password = admin.getPassword();
            this.role = "ROLE_CADMIN";
            this.enabled = admin.isEnabled();
        }
    }

//    public UserDTO(Customer customer) {
//        if(customer != null) {
//            this.id = customer.getId();
//            this.fullName = customer.getFullName();
//            this.company = null;
//            this.dateOfBirth = customer.getDateOfBirth();
//            this.address = customer.getAddress();
////        this.password = customer.getPassword();
//            this.role = "ROLE_CUSTOMER";
//            this.enabled = customer.isEnabled();
//        }
//    }

//    public UserDTO(Employee employee) {
//        if(employee != null) {
//            this.id = employee.getId();
//            this.fullName = employee.getFullName();
//            this.company = new CompanyDTO(employee.getCompany());
//            this.dateOfBirth = employee.getDateOfBirth();
//            this.address = employee.getAddress();
////        this.password = employee.getPassword();
//            this.role = "ROLE_EMPLOYEE";
//            this.enabled = employee.isEnabled();
//        }
//    }

    public UserDTO(User user) {
        if(user != null) {
            this.id = user.getId();
            this.fullName = user.getFullName();
            this.dateOfBirth = user.getDateOfBirth();
            this.address = user.getAddress();
//        this.password = user.getPassword();
            this.role = user.getRoles().iterator().next().getName();
            this.enabled = user.isEnabled();
        }
    }
}
