package com.example.case_study.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "message", required = false) String message, Model model) {
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "login/login"; // Trả về trang đăng nhập
    }

    @GetMapping("/login-success")
    public String showLoginSuccess(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (roles.contains("ROLE_ADMIN")) {
            return "redirect:/admin/home";
        } else if (roles.contains("ROLE_USER")) {
            return "redirect:/user/home";
        }

        return "redirect:/home";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Bạn đã đăng xuất thành công.");
        return "redirect:/login";
    }
}
