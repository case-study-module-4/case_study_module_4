package com.example.case_study.controller;

import com.example.case_study.model.Account;
import com.example.case_study.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/update-password")
    public String showUpdatePasswordForm() {
        return "account/updateForm";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        Account account = accountService.findByUsername(principal.getName());
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại!");
            return "redirect:/account/update-password";
        }

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không chính xác!");
            return "redirect:/account/update-password";
        }

        // Kiểm tra mật khẩu mới có khớp với xác nhận không
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/account/update-password";
        }

        // Cập nhật mật khẩu mới
        accountService.updatePassword(account, newPassword);
        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        return "redirect:/dashboard";
    }
}
