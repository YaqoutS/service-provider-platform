package com.graduationproject.serviceproviderplatform.repository;

import com.graduationproject.serviceproviderplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFullName(String fullName);

    Optional<User> findByEmailAndFullName(String email, String fullName);
    boolean existsByEmail(String email);

    boolean existsByFullName(String fullName);
    //Optional<User> findByEmailAndActivationCode(String email, String activationCode);
}
