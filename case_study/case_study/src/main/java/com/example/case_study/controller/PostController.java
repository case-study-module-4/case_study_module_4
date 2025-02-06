package com.example.case_study.controller;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import com.example.case_study.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping()
public class PostController {

    @Autowired
    private IPostService postService;

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

    @GetMapping("/user/posts/approved")
    public String showApprovedPosts(Model model, Integer userId) {
        model.addAttribute("posts", postService.getApprovedPostsByUser(userId));
        return "user/approved-posts"; // Trả về trang HTML danh sách tin đã phê duyệt
    }

    @GetMapping("/user/posts/drafts")
    public String showDraftPosts(Model model, Integer userId) {
        model.addAttribute("posts", postService.getDraftPostsByUser(userId));
        return "user/draft-posts"; // Trả về trang HTML danh sách tin nháp
    }

    @GetMapping("/user/posts/{id}/details")
    public String showPostDetails(@PathVariable("id") Integer id, Model model) {
        PostDTO post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "user/post-details"; // Trả về trang chi tiết bài đăng
    }
}

