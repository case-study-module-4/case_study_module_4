package com.example.case_study.controller;

import com.example.case_study.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @PostMapping("/update-password")
    public String updatePassword(
            Principal principal,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            Model model) {

        String username = principal.getName();

        // Kiểm tra mật khẩu cũ trước khi cập nhật
        boolean isUpdated = accountService.updateAccountPassword(username, currentPassword, newPassword);

        if (!isUpdated) {
            model.addAttribute("error", "Mật khẩu hiện tại không đúng.");
            return "account/updatePassword";
        }

        // Lấy quyền của người dùng hiện tại
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        model.addAttribute("success", "Đổi mật khẩu thành công!");
        model.addAttribute("userRole", role);
        return "account/updatePassword";
    }
}
