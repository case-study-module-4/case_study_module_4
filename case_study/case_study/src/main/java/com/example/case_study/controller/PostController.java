package com.example.case_study.controller;


import com.example.case_study.dto.PostDTO;
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

    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post/post-sale";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        PostDTO postDTO = new PostDTO();
        postDTO.setStatus("Available");
        model.addAttribute("statusDisplay", "Chưa Giao Dịch");
        model.addAttribute("postDTO", postDTO);  // Sử dụng đối tượng đã được cấu hình
        model.addAttribute("purposes", purposeService.findAll());
        model.addAttribute("realEstates", realEstateService.findAll());

        return "post/create-post";
    }


    @PostMapping()
    public String createPost(@Valid @ModelAttribute("postDTO") PostDTO postDTO,
                             BindingResult result,
                             Model model,
                             Principal principal) { // thêm Principal vào đây
        model.addAttribute("postDTO", new PostDTO());
        model.addAttribute("purposes", purposeService.findAll());
        model.addAttribute("realEstates", realEstateService.findAll());

        if (result.hasErrors()) {
            return "post/create-post";
        }
        // Lấy user hiện hành dựa vào principal
        User user = userService.findUserByUsername(principal.getName());
        // Truyền user vào service để gán cho bài đăng
        postService.createPost(postDTO, user);
        return "redirect:/home";
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
            if (post.getPurpose() != null) {
                postDTO.setPurpose(post.getPurpose().getPurpose());
            } else {
                postDTO.setPurpose("");
            }
            postDTO.setType(post.getRealEstate().getType());
            postDTO.setLocation(post.getRealEstate().getLocation());
            postDTO.setArea(post.getRealEstate().getArea());
            postDTO.setDirection(post.getRealEstate().getDirection());
            postDTO.setPrice(post.getRealEstate().getPrice());
            // Gán ảnh chính
            postDTO.setImage(post.getImage());
            // Gán danh sách ảnh phụ (nếu có)
            postDTO.setImages(post.getImages());

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
                             @RequestParam(value = "deleteImages", required = false) List<Integer> deleteImageIds) {
        if (result.hasErrors()) {
            model.addAttribute("purposes", purposeService.findAll());
            model.addAttribute("realEstates", realEstateService.findAll());
            return "post/edit";
        }
        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            User user = userService.findUserByUsername(principal.getName());
            // Kiểm tra quyền sở hữu bài đăng (nếu cần)
            if (post.getUser() == null || !post.getUser().equals(user)) {
                return "redirect:/403";
            }

            // Gọi phương thức updatePost để cập nhật đầy đủ các trường
            postService.updatePost(post, postDTO, deleteImageIds);
            return "redirect:/posts";
        }
        return "redirect:/posts?error=notfound";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Integer id, Principal principal) {
        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            User user = userService.findUserByUsername(principal.getName());
            // Kiểm tra quyền sở hữu bài đăng
            if (post.getUser() == null || !post.getUser().equals(user)) {
                return "redirect:/403"; // Hoặc trả về view lỗi phù hợp
            }
            postService.deleteById(id);
        }
        return "redirect:/posts?message=deleted";
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
    @GetMapping("/rent")
    public String getRentPosts(Model model) {
        // Lấy danh sách tất cả các bài đăng
        List<Post> allPosts = postService.findAll();
        // Lọc các bài đăng có purpose là "RENT"
        List<Post> rentPosts = allPosts.stream()
                .filter(post -> post.getPurpose() != null
                        && post.getPurpose().getPurpose() != null
                        && post.getPurpose().getPurpose().equalsIgnoreCase("RENT"))
                .collect(Collectors.toList());
        model.addAttribute("posts", rentPosts);
        return "post/post-rent"; // Template dành cho bài đăng cho thuê
    }
    @GetMapping("/sale")
    public String getSalePosts(Model model) {
        // Lấy danh sách tất cả các bài đăng
        List<Post> allPosts = postService.findAll();
        // Lọc các bài đăng có purpose là "SALE"
        List<Post> rentPosts = allPosts.stream()
                .filter(post -> post.getPurpose() != null
                        && post.getPurpose().getPurpose() != null
                        && post.getPurpose().getPurpose().equalsIgnoreCase("SALE"))
                .collect(Collectors.toList());
        model.addAttribute("posts", rentPosts);
        return "post/post-sale"; //
    }

}
