package com.example.case_study.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.case_study.model.Account;
import com.example.case_study.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class LoginController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "message", required = false) String message, Model model) {
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "login/login"; // Trả về trang đăng nhập
    }

    // Hỗ trợ cả POST (do forward từ filter) và GET nếu cần
    @RequestMapping(value = "/login-success", method = {RequestMethod.GET, RequestMethod.POST})
    public String showLoginSuccess(Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null) {
            return "redirect:/login";
        }

        Account account = accountService.findByUsername(authentication.getName());
        if (account == null) {
            return "redirect:/login";
        }

        // Kiểm tra nếu tài khoản bị khóa
        if ("inactive".equals(account.getStatus())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản của bạn đã bị khóa. Liên hệ Admin để mở khóa.");
            return "redirect:/account-locked";
        }

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_USER")) {
            return "redirect:/home";
        }

        return "redirect:/home";
    }

    @GetMapping("/account-locked")
    public String lockedAccount(Model model) {
        model.addAttribute("supportEmail", "phuong100998@gmail.com");
        return "user/account-locked";
    }
}
