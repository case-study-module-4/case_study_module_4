//package com.example.case_study.controller.account;
//
//import com.example.case_study.service.acounnt.IAccountService;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/accounts")
//public class AccountController {
//
//    @Autowired
//    private IAccountService accountService;
//
//    @Transactional
//    @PostMapping("/update-password")
//    public String updatePassword(Principal principal, @RequestParam String newPassword, Model model) {
//        String username = principal.getName();
//        accountService.updateAccount(username, newPassword);
//
//        // Lấy quyền của người dùng hiện tại
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String role = auth.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .findFirst()
//                .orElse("ROLE_USER");
//
//        model.addAttribute("userRole", role);
//        return "account/updatePassword";
//    }
//}