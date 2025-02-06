package com.example.case_study.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @GetMapping
    public String showRegisterForm() {
        return "login/register";
    }

    @PostMapping
    public String processRegister(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "login/register";
        }

        model.addAttribute("message", "Đăng ký thành công! Hãy đăng nhập.");
        return "login/login";
    }
}
