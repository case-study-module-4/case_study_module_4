package com.example.case_study.controller;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Account;
import com.example.case_study.model.Post;
import com.example.case_study.model.User;
import com.example.case_study.repository.ImageRepository;
import com.example.case_study.service.*;
import com.example.case_study.service.impl.PostService;
import com.example.case_study.service.impl.PurposeService;
import com.example.case_study.service.impl.RealEstateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PurposeService purposeService;

    @Autowired
    private RealEstateService realEstateService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IPostInterestService postInterestService;

    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post/post";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        PostDTO postDTO = new PostDTO();
        postDTO.setStatus("Available");
        model.addAttribute("statusDisplay", "Chưa Giao Dịch");
        model.addAttribute("postDTO", postDTO);
        model.addAttribute("purposes", purposeService.findAll());
        model.addAttribute("realEstates", realEstateService.findAll());
        return "post/create-post";
    }

    @PostMapping()
    public String createPost(@Valid @ModelAttribute("postDTO") PostDTO postDTO,
                             BindingResult result,
                             Model model,
                             Principal principal,
                             @RequestParam(value = "action", required = false, defaultValue = "continue") String action) {
        model.addAttribute("postDTO", new PostDTO());
        model.addAttribute("purposes", purposeService.findAll());
        model.addAttribute("realEstates", realEstateService.findAll());

        if (result.hasErrors()) {
            return "post/create-post";
        }

        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);

        Post createdPost = postService.createPost(postDTO, user);

        if ("continue".equalsIgnoreCase(action)) {
            return "redirect:/transaction/transaction?postId=" + createdPost.getId();
        } else if ("save".equalsIgnoreCase(action)) {
            return "redirect:/posts/drafts?message=saved";
        }
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setStatus(post.getStatus());
            postDTO.setPurpose(post.getPurpose() != null ? post.getPurpose().getPurpose() : "");
            postDTO.setType(post.getRealEstate().getType());
            postDTO.setLocation(post.getRealEstate().getLocation());
            postDTO.setArea(post.getRealEstate().getArea());
            postDTO.setDirection(post.getRealEstate().getDirection());
            postDTO.setPrice(post.getRealEstate().getPrice());
            postDTO.setImage(post.getImage());
            postDTO.setImages(post.getImages());
            postDTO.setPayable(post.getPayable());
            model.addAttribute("post", postDTO);
            return "post/edit";
        }
        return "redirect:/posts?error=notfound";
    }

    @PostMapping("/{id}")
    public String updatePost(@PathVariable Integer id,
                             Principal principal,
                             @Valid @ModelAttribute("post") PostDTO postDTO,
                             BindingResult result,
                             Model model,
                             @RequestParam(value = "deleteImages", required = false) List<Integer> deleteImageIds,
                             @RequestParam(value = "action", required = false, defaultValue = "update") String action) {
        if (result.hasErrors()) {
            model.addAttribute("purposes", purposeService.findAll());
            model.addAttribute("realEstates", realEstateService.findAll());
            return "post/edit";
        }

        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            User user = userService.findUserByUsername(principal.getName());
            if (post.getUser() == null || !post.getUser().equals(user)) {
                return "redirect:/403";
            }
            if (postDTO.getPrice() == null) {
                postDTO.setPrice(post.getRealEstate().getPrice());
            }
            postService.updatePost(post, postDTO, deleteImageIds);
            if ("continue".equalsIgnoreCase(action)) {
                return "redirect:/transaction/transaction?postId=" + post.getId();
            } else {
                return "redirect:/posts/approved";
            }
        }
        return "redirect:/posts?error=notfound";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            User user = userService.findUserByUsername(principal.getName());
            if (post.getUser() == null || !post.getUser().equals(user)) {
                return "redirect:/403";
            }
            postService.deleteById(id);
            if ("no".equalsIgnoreCase(post.getPayable())) {
                redirectAttributes.addFlashAttribute("message", "Xóa bài đăng thành công!");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return "redirect:/posts/drafts?message=deleted";
            } else {
                redirectAttributes.addFlashAttribute("message", "Xóa bài đăng thành công!");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return "redirect:/posts/approved?message=deleted";
            }
        }
        redirectAttributes.addFlashAttribute("message", "Xóa bài đăng không thành công!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        return "redirect:/posts?error=notfound";
    }

    @GetMapping("/approved")
    public String getApprovedPostsForUser(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);

        // Lấy tất cả bài đăng đã thanh toán (và chưa bị xóa)
        List<Post> allApprovedPosts = postService.findAll();
        List<Post> approvedPosts = allApprovedPosts.stream()
                .filter(post -> post.getUser() != null && post.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
        model.addAttribute("userId", user.getId());
        model.addAttribute("user", user);
        model.addAttribute("posts", approvedPosts);

        return "user/approved-posts";
    }

    @GetMapping("/drafts")
    public String getDraftPostsForUser(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        List<Post> draftPosts = postService.getDraftPostsByUserId(user.getId());
        model.addAttribute("userId", user.getId());
        model.addAttribute("user", user);
        model.addAttribute("posts", draftPosts);
        return "user/drafts-posts";
    }

    @GetMapping("/{id}")
    public String viewPostDetail(@PathVariable Integer id, Model model, Principal principal) {
        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            // (Tùy chọn) Nếu bài đăng đã soft delete, có thể chuyển hướng về trang 404
            if (post.isDeleted()) {
                return "redirect:/posts?error=notfound";
            }
            model.addAttribute("post", post);
            if (principal != null) {
                String username = principal.getName();
                Account account = accountService.findByUsername(username);
                postInterestService.logPostClick(id, account);
            }
            return "post/detail";
        }
        return "redirect:/posts?error=notfound";
    }

    @GetMapping("/rent")
    public String getRentPosts(Model model) {
        List<Post> allPosts = postService.findAll();
        List<Post> rentPosts = allPosts.stream()
                .filter(post -> post.getPurpose() != null
                        && post.getPurpose().getPurpose() != null
                        && post.getPurpose().getPurpose().equalsIgnoreCase("RENT"))
                .collect(Collectors.toList());
        model.addAttribute("posts", rentPosts);
        return "post/post";
    }

    @GetMapping("/sale")
    public String getSalePosts(Model model) {
        List<Post> allPosts = postService.findAll();
        List<Post> salePosts = allPosts.stream()
                .filter(post -> post.getPurpose() != null
                        && post.getPurpose().getPurpose() != null
                        && post.getPurpose().getPurpose().equalsIgnoreCase("SALE"))
                .collect(Collectors.toList());
        model.addAttribute("posts", salePosts);
        return "post/post";
    }
}
