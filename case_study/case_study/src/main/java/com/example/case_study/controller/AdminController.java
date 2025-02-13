package com.example.case_study.controller;

import com.example.case_study.dto.AccountDTO;
import com.example.case_study.dto.DepositHistoryDto;
import com.example.case_study.dto.TransactionHistoryDto;
import com.example.case_study.model.User;
import com.example.case_study.repository.PostTypeRepository;
import com.example.case_study.service.IDepositService;
import com.example.case_study.service.ITransactionService;
import com.example.case_study.service.IUserService;
import com.example.case_study.service.impl.AccountService;
import com.example.case_study.service.impl.PostService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/account")
public class AdminController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostTypeRepository postTypeRepository;

    @Autowired
    private IDepositService depositService;
    @GetMapping
    public String getAllAccounts(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return "redirect:/error";
        }

        List<AccountDTO> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        model.addAttribute("userId", user.getId());
        model.addAttribute("user", user);
        return "admin/account-list";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleAccountStatus(@PathVariable Integer id) {
        accountService.toggleAccountStatus(id);
        return "redirect:/admin/account";
    }

    @GetMapping("/transaction-history")
    public String getTransactionHistoryAllUser(Model model) {
        List<DepositHistoryDto> deposits = depositService.getAllDepositHistory();
        List<TransactionHistoryDto> payments = transactionService.getAllTransactionHistory();


        model.addAttribute("deposits", deposits);
        model.addAttribute("payments", payments);

        return "transaction/transaction-history";
    }
}
