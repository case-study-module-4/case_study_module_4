package com.example.case_study.controller;

import com.example.case_study.dto.AccountRegisterDTO;
import com.example.case_study.service.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final IAccountService accountService;

    @GetMapping("")
    public String showRegisterForm(Model model) {
        model.addAttribute("accountDTO", new AccountRegisterDTO());
        return "login/register";
    }

    @PostMapping("")
    public String processRegister(@Valid @ModelAttribute("accountDTO") AccountRegisterDTO accountDTO,
                                  BindingResult result,
                                  @RequestParam String confirmPassword,
                                  Model model) {
        if (result.hasErrors()) {
            return "login/register";
        }

        String responseMessage = accountService.registerAccount(accountDTO, confirmPassword);
        if (responseMessage.equals("Đăng ký thành công! Hãy đăng nhập.")) {
            model.addAttribute("message", responseMessage);
            return "login/login";
        } else {
            model.addAttribute("error", responseMessage);
            return "login/register";
        }
    }
}
