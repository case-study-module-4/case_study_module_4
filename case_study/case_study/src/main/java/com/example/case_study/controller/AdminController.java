package com.example.case_study.controller;

import com.example.case_study.dto.AccountDTO;
import com.example.case_study.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    public String getAllAccounts(Model model) {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "admin/account-list";
    }

    @PostMapping("/account/delete/{id}")
    public String deleteAccount(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean deleted = accountService.softDeleteAccount(id);
        if (!deleted) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản");
        } else {
            redirectAttributes.addFlashAttribute("success", "Xóa tài khoản thành công");
        }
        return "redirect:/admin";
    }
}
