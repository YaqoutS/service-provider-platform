package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.User;
import com.graduationproject.serviceproviderplatform.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Here I may add the API for changing the password

    @GetMapping("/{id}/chat-token")
    public ResponseEntity<String> getChatToken(@PathVariable Long id) {
        if(!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no user with id = " + id);
        }
        User user = userRepository.findById(id).get();
        return ResponseEntity.ok(user.getChatToken());
    }

    @PutMapping("/{id}/chat-token/{chatToken}")
    public ResponseEntity<String> setChatToken(@PathVariable Long id, @PathVariable String chatToken) {
        if(!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no user with id = " + id);
        }
        User user = userRepository.findById(id).get();
        user.setChatToken(chatToken);
        user.setConfirmPassword(user.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok("Chat token added successfully");
    }

    @GetMapping("/{id}/notification-token")
    public ResponseEntity<String> getNotificationToken(@PathVariable Long id) {
        if(!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no user with id = " + id);
        }
        User user = userRepository.findById(id).get();
        return ResponseEntity.ok(user.getNotificationToken());
    }

    @PutMapping("/{id}/notification-token/{notificationToken}")
    public ResponseEntity<String> setNotificationToken(@PathVariable Long id, @PathVariable String notificationToken) {
        if(!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no user with id = " + id);
        }
        User user = userRepository.findById(id).get();
        user.setNotificationToken(notificationToken);
        user.setConfirmPassword(user.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok("Notification token added successfully");
    }
}
