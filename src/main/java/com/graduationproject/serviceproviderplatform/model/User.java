package com.graduationproject.serviceproviderplatform.model;

import com.graduationproject.serviceproviderplatform.model.validator.PasswordMatch;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="users")
@Inheritance(strategy = InheritanceType.JOINED)
@PasswordMatch
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String fullName;

    @NonNull
    @Size(min = 8, max = 30)
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(length = 100)
    private String password;

    @Transient
    private String confirmPassword;

    @Past(message = "Date of birth must be in the past.")
    private LocalDate dateOfBirth;

    @Transient
    private int age;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    private String phone;
    
//    private String activationCode;

    @NonNull
    @Column(nullable = false)
    private boolean enabled;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();


    @PostLoad
    private void calculateAge() {
        if (dateOfBirth != null) {
            age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//    }

    public void addRole(Role role) {
        roles.add(role);
    }

//    @Override
//    public String getUsername() {
//        return fullName;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
}
