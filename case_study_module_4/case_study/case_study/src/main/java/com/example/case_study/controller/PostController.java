package com.example.case_study.controller;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import com.example.case_study.service.IPostService;
import com.example.case_study.service.IPurposeService;
import com.example.case_study.service.IRealEstateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private IPurposeService purposeService;

    @Autowired
    private IRealEstateService realEstateService;

    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post/post";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("postDTO", new PostDTO()); // Đảm bảo postDTO được truyền vào model
        model.addAttribute("purposes", purposeService.findAll());
        model.addAttribute("realEstates", realEstateService.findAll());
        return "post/create-post";
    }

    @PostMapping
    public String createPost(@Valid @ModelAttribute("postDTO") PostDTO postDTO,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("purposes", purposeService.findAll());
            model.addAttribute("realEstates", realEstateService.findAll());
            return "post/create-post"; // Quay lại trang create nếu có lỗi
        }

        postService.createPost(postDTO);
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
}