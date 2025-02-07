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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.security.Principal;
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
                             @RequestParam("image") MultipartFile image,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("purposes", purposeService.findAll());
            model.addAttribute("realEstates", realEstateService.findAll());
            return "post/create-post";
        }

        if (!image.isEmpty()) {
            postDTO.setImage(image);
        }

        // Kiểm tra và thiết lập giá trị mặc định cho status nếu nó bị trống
        if (postDTO.getStatus() == null || postDTO.getStatus().isBlank()) {
            postDTO.setStatus("Pending"); // hoặc giá trị mặc định khác
        }

        // Kiểm tra và thiết lập giá trị mặc định cho publishDate nếu nó bị null
        if (postDTO.getPublishDate() == null) {
            postDTO.setPublishDate(LocalDate.now()); // hoặc giá trị mặc định khác
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

}
