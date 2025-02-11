package com.example.case_study.controller;

import com.example.case_study.model.Account;
import com.example.case_study.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/update-password")
    public String showUpdatePasswordForm() {
        return "account/updateForm"; // Đảm bảo file nằm trong templates/account/
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/account/update-password";
        }

        Account account = accountService.findByUsername(principal.getName());
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại!");
            return "redirect:/account/update-password";
        }

        accountService.updatePassword(account, newPassword);
        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        return "redirect:/dashboard";
    }
}
