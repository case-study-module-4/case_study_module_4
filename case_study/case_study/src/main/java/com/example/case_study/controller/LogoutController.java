package com.example.case_study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logoutSuccessful(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Bạn đã đăng xuất thành công.");
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminLogoutSuccessful(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Bạn đã đăng xuất khỏi trang quản trị.");
        return "redirect:/login";
    }
}
