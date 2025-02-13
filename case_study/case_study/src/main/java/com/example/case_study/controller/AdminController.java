package com.example.case_study.controller;

import com.example.case_study.dto.AccountDTO;
import com.example.case_study.service.impl.AccountService;
import jakarta.persistence.EntityManager;
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
@RequestMapping("/admin/account")
public class AdminController {
    @Autowired
    private AccountService accountService;


    @GetMapping
    public String getAllAccounts(Model model) {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "admin/account-list";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleAccountStatus(@PathVariable Integer id) {
        accountService.toggleAccountStatus(id);
        return "redirect:/admin/account";
    }
}
