package com.example.case_study.controller;

import com.example.case_study.service.impl.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String name) {
        try {
            String token = generateVerificationToken(email); // Hàm tạo token xác thực
            emailService.sendVerificationEmail(email, name, token);
            return "Email xác thực đã được gửi.";
        } catch (MessagingException e) {
            return "Lỗi khi gửi email: " + e.getMessage();
        }
    }

    private String generateVerificationToken(String email) {
        return email.hashCode() + "-" + System.currentTimeMillis();
    }
}
