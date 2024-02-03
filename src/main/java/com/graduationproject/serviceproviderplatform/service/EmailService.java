package com.graduationproject.serviceproviderplatform.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendPasswordResetEmail(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
//        message.setFrom("admin@gmail.com");
        message.setSubject("Password Reset");
        message.setText("Your new password is: " + newPassword);

        javaMailSender.send(message);
    }
}
