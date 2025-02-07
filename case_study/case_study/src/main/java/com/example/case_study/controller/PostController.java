package com.example.case_study.controller;


import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import com.example.case_study.model.User;
import com.example.case_study.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private IPurposeService purposeService;

    @Autowired
    private IRealEstateService realEstateService;


    @Autowired
    private IUserService userService;

    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post/post-sale";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("postDTO", new PostDTO()); // Đảm bảo postDTO được truyền vào model
        model.addAttribute("purposes", purposeService.findAll());
        model.addAttribute("realEstates", realEstateService.findAll());

        return "post/create-post";
    }

    @PostMapping()
    public String createPost(@Valid @ModelAttribute("postDTO") PostDTO postDTO,
                             BindingResult result,
                             Model model) {
        model.addAttribute("postDTO", new PostDTO());
        model.addAttribute("purposes", purposeService.findAll());
        model.addAttribute("realEstates", realEstateService.findAll());

        if (result.hasErrors()) {
            return "post/create-post";
        }
        postService.createPost(postDTO);
        return "redirect:/home";
    }


    @PutMapping("/{id}")
    public String updatePost(@PathVariable Integer id, @Valid @ModelAttribute Post post, BindingResult result) {
        if (result.hasErrors()) {
            return "post/edit-post";
        }

        if (postService.findById(id) == null) {
            return "redirect:/posts?error=notfound";
        }

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
        model.addAttribute("approvedPosts", approvedPosts);

        return "user/approved-posts";
    }

    @GetMapping("/drafts")
    public String getDraftPostsForUser(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        List<Post> draftPosts = postService.getDraftPostsByUserId(user.getId());
        model.addAttribute("draftPosts", draftPosts);

        return "user/drafts-posts";
    }
    @GetMapping("/{id}")
    public String viewPostDetail(@PathVariable Integer id, Model model) {
        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            model.addAttribute("post", post);
            String imageBase64 = post.getImage();
            if (imageBase64 != null) {
                String imageDataUrl = "data:image/jpeg;base64," + imageBase64;
                model.addAttribute("imageDataUrl", imageDataUrl);
            }
            return "post/detail";
        }
        return "redirect:/posts?error=notfound";
    }
}
