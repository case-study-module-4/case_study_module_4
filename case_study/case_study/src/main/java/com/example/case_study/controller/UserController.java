package com.example.case_study.controller;

import com.example.case_study.model.Post;
import com.example.case_study.model.User;
import com.example.case_study.service.IUserService;

import com.example.case_study.service.impl.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private PostService postService;

//    @Autowired
//    private FileStorageService fileStorageService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return "redirect:/error";
        }
        model.addAttribute("userId", user.getId());
        model.addAttribute("user", user);
//        model.addAttribute("success", success);
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
                                           @RequestParam(value = "image", required = false) MultipartFile image,
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
//        if (image != null && !image.isEmpty()) {
//            try {
//                String imagePath = fileStorageService.storeFile(image); // Lưu ảnh và trả về đường dẫn
//                existingUser.setImage(imagePath);
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("Lỗi khi lưu ảnh: " + e.getMessage());
//            }
//        }
        userService.updateUser(existingUser);

        return ResponseEntity.ok("Cập nhật thông tin thành công!");
    }
    @GetMapping("/user/{id}")
    public String viewUserProfile(@PathVariable("id") Integer id, Model model) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            // Lấy danh sách bài đăng của user này
            // Nếu có method riêng trong IPostService, ví dụ: List<Post> posts = postService.findByUserId(id);
            // Nếu không, ta có thể lọc từ findAll() (chỉ dùng trong trường hợp dữ liệu không quá lớn)
            List<Post> posts = postService.findAll().stream()
                    .filter(post -> post.getUser() != null && post.getUser().getId().equals(id))
                    .collect(Collectors.toList());
            model.addAttribute("posts", posts);
            return "user/profile"; // Trả về template user/profile.html
        } else {
            return "redirect:/posts?error=userNotFound";
        }
    }


}

