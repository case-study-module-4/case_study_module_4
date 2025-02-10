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
        // Thiết lập lại các đối tượng cho view (nếu có lỗi)
        model.addAttribute("postDTO", new PostDTO());
        model.addAttribute("purposes", purposeService.findAll());
        model.addAttribute("realEstates", realEstateService.findAll());

        if (result.hasErrors()) {
            return "post/create-post";
        }

        // Lấy user hiện hành
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);

        // Tạo bài viết; lưu ý trong service, khi tạo bài viết, giá trị payable được gán mặc định là "no"
        Post createdPost = postService.createPost(postDTO, user);

        // Nếu action là "continue" -> chuyển hướng đến trang transaction để thanh toán
        // Nếu action là "save" -> chuyển hướng về trang danh sách nháp (hoặc trang bạn muốn)
        if ("continue".equalsIgnoreCase(action)) {
            return "redirect:/transaction/transaction?postId=" + createdPost.getId();
        } else if ("save".equalsIgnoreCase(action)) {
            return "redirect:/posts/drafts?message=saved";
        }

        // Mặc định (nếu không có action phù hợp) chuyển về trang chủ hoặc danh sách bài viết
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
                             @RequestParam(value = "action", required = false, defaultValue = "update") String action) {  // nhận thêm tham số "action"
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

            // Gọi phương thức updatePost để cập nhật đầy đủ các trường (lưu ý: không thay đổi trạng thái payable)
            postService.updatePost(post, postDTO, deleteImageIds);

            // Nếu bài post có payable = "no" và người dùng bấm nút "Tiếp tục" thì điều hướng tới trang giao dịch
            if ("continue".equalsIgnoreCase(action)) {
                // Điều hướng tới trang giao dịch, truyền postId cần thanh toán (transaction.html)
                return "redirect:/transaction/transaction?postId=" + post.getId();
            } else {
                // Ngược lại (payable = yes hoặc hành động là cập nhật) thì về trang danh sách bài viết
                return "redirect:/posts";
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
                return "redirect:/posts?message=deleted";
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
        List<Post> approvedPosts = postService.findAll();
        model.addAttribute("posts", approvedPosts);

        return "user/approved-posts";
    }

    @GetMapping("/drafts")
    public String getDraftPostsForUser(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        // Lấy các bài viết của user có payable = "no"
        List<Post> draftPosts = postService.getDraftPostsByUserId(user.getId());
        model.addAttribute("posts", draftPosts);
        return "user/drafts-posts"; // Giao diện hiển thị bài viết nháp
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
        List<Post> allPosts = postService.findAll();
        // Lọc các bài đăng có purpose là "RENT"
        List<Post> rentPosts = allPosts.stream()
                .filter(post -> post.getPurpose() != null
                        && post.getPurpose().getPurpose() != null
                        && post.getPurpose().getPurpose().equalsIgnoreCase("RENT"))
                .collect(Collectors.toList());
        model.addAttribute("posts", rentPosts);
        return "post/post"; // Template dành cho bài đăng cho thuê
    }
    @GetMapping("/sale")
    public String getSalePosts(Model model) {
        List<Post> allPosts = postService.findAll();
        // Lọc các bài đăng có purpose là "SALE"
        List<Post> rentPosts = allPosts.stream()
                .filter(post -> post.getPurpose() != null
                        && post.getPurpose().getPurpose() != null
                        && post.getPurpose().getPurpose().equalsIgnoreCase("SALE"))
                .collect(Collectors.toList());
        model.addAttribute("posts", rentPosts);
        return "post/post"; //
    }

}
