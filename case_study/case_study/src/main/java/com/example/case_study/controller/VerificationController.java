package com.example.case_study.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class VerificationController {

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token) {
        if (isValidToken(token)) {
            return "Xác thực thành công!";
        } else {
            return "Token không hợp lệ hoặc đã hết hạn!";
        }
    }

    private boolean isValidToken(String token) {
        return token != null && token.length() > 10;
    }
}
