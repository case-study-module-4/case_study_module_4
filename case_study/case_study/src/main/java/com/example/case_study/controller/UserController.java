package com.example.case_study.controller;

import com.example.case_study.model.User;
import com.example.case_study.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        model.addAttribute("user", user);
        return "user/dashboard";
    }

    @GetMapping("/profile/edit")
    public String showEditForm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    @PostMapping("/profile/update")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@Valid @RequestBody User user,
                                           BindingResult bindingResult,
                                           Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập để thực hiện thao tác này.");
        }

        if (bindingResult.hasErrors()) {
            // Trả về lỗi dưới dạng JSON
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        String username = principal.getName();
        User existingUser = userService.findUserByUsername(username);

        existingUser.setFullName(user.getFullName());
        existingUser.setPhone(user.getPhone());
        userService.updateUser(existingUser);

        return ResponseEntity.ok("Cập nhật thông tin thành công!");
    }


}
