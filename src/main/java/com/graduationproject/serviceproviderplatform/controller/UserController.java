package com.graduationproject.serviceproviderplatform.controller;

import com.graduationproject.serviceproviderplatform.model.Image;
import com.graduationproject.serviceproviderplatform.model.User;
import com.graduationproject.serviceproviderplatform.repository.UserRepository;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private UserRepository userRepository;
    private final ResourceLoader resourceLoader;

    public UserController(UserRepository userRepository,ResourceLoader resourceLoader) {

        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
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

    @PostMapping("/{userId}/uploadImage")
    public ResponseEntity<String> handleFileUpload(@RequestParam("image") MultipartFile image, @PathVariable Long userId) {
        User user = userRepository.findById(userId).get();
        Image userImage=new Image("usersImages/" + userId.intValue() + ".jpg");
        user.setImage(userImage);
        userRepository.save(user);
        System.out.println("image recieved");
        try {
            URI resourceUri = resourceLoader.getResource("classpath:").getURI();
            String decodedResourcePath = resourceUri.getPath();
            String ServiceImagesRoot = decodedResourcePath.substring(1);
            String relativePath = "assets/usersImages/" + userId.intValue() + ".jpg";
            System.out.println(ServiceImagesRoot+relativePath);
            Path absolutePath = Paths.get(ServiceImagesRoot+relativePath);
            Files.write(absolutePath, image.getBytes());
            return ResponseEntity.ok("Image uploaded successfully. Path: " + relativePath);
        } catch (IOException e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
        }
    }
}
