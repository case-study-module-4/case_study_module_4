package com.example.case_study.controller;

import com.example.case_study.model.Post;
import com.example.case_study.model.User;
import com.example.case_study.service.IPostService;
import com.example.case_study.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post/post";
    }

    @PostMapping
    public String createPost(@ModelAttribute Post post) {
        postService.save(post);
        return "redirect:/posts";
    }

    @PostMapping("/{id}")
    public String updatePost(@PathVariable Integer id, @ModelAttribute Post post) {
        post.setId(id);
        postService.save(post);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Integer id) {
        postService.deleteById(id);
        return "redirect:/posts";
    }

    @GetMapping("/approved")
    public String getApprovedPostsForUser(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        List<Post> approvedPosts = postService.getApprovedPostsByUserId(user.getId());
        model.addAttribute("posts", approvedPosts);

        return "user/approved-posts";
    }

    @GetMapping("/drafts")
    public String getDraftPostsForUser(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        List<Post> draftPosts = postService.getDraftPostsByUserId(user.getId());
        model.addAttribute("posts", draftPosts);

        return "user/drafts-posts";
    }

}

