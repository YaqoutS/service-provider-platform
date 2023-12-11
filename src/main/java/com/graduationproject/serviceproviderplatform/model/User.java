package com.graduationproject.serviceproviderplatform.model;

import com.graduationproject.serviceproviderplatform.model.validator.PasswordMatch;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="users")
@PasswordMatch
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Size(min = 8, max = 30)
    @Column(nullable = false, unique = true)
    private String email;

//    @NonNull
//    @NotEmpty(message = "First Name can't be empty.")
//    private String firstName;

//    @NonNull
//    @NotEmpty(message = "Last Name can't be empty.")
//    private String lastName;

//    @Transient
//    @Setter(AccessLevel.NONE)
    @NonNull
    private String fullName;

    @Past(message = "Date of birth must be in the past.")
    private LocalDate dateOfBirth;

    @Transient
    private int age;

    private String location;

    @NonNull
    @Column(length = 100)
    private String password;

    @Transient
    private String confirmPassword;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @PostLoad
    private void calculateAge() {
        if (dateOfBirth != null) {
            age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
    }

    public String getConfirmPassword(){
        return confirmPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addRoles(Set<Role> roles) {
        roles.forEach(this::addRole);
    }

    @Override
    public String getUsername() {
        return fullName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
