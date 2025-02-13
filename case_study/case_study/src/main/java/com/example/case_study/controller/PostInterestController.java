package com.example.case_study.controller;

import com.example.case_study.model.PostInterest;
import com.example.case_study.model.User;
import com.example.case_study.service.IPostInterestService;
import com.example.case_study.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user/interests")
public class PostInterestController {

    @Autowired
    private IPostInterestService postInterestService;
    @Autowired
    private IUserService userService;

    @GetMapping
    public String getUserInterests(@RequestParam("userId") Integer userId, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User loggedInUser = userService.findUserByUsername(username);

        // Đảm bảo chỉ người đăng bài mới được xem danh sách người quan tâm
        if (!loggedInUser.getId().equals(userId)) {
            return "redirect:/error";
        }
        List<PostInterest> interests = postInterestService.getPostInterestsByOwnerId(userId);
        model.addAttribute("interests", interests);
        return "user/interests";
    }
}