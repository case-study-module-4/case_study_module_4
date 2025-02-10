    package com.example.case_study.controller;

    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;

    import java.util.Set;
    import java.util.stream.Collectors;

    @Controller
    public class LoginController {

        @GetMapping("/login")
        public String showLoginForm() {
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
    }
