package com.example.case_study.controller;

import com.example.case_study.model.User;
import com.example.case_study.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping
public class UserController {
    @Autowired
    private IUserService userService;
    @GetMapping("/user/dashboard")
    public String showDashboard(Model model) {
        // Tạo một đối tượng User giả định để hiển thị thông tin người dùng
        User user = new User();
        user.setUsername("User123"); // Tên người dùng giả định
        user.setBalance(new BigDecimal("1000.00")); // Số dư tài khoản giả định
        model.addAttribute("user", user);
        return "user/dashboard";
    }
}
