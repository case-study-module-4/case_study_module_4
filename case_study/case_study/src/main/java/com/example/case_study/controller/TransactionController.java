package com.example.case_study.controller;

import com.example.case_study.model.Post;
import com.example.case_study.model.Transaction;
import com.example.case_study.model.User;
import com.example.case_study.service.ITransactionService;
import com.example.case_study.service.IUserService;
import com.example.case_study.service.impl.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private PostService postService;


    @GetMapping("/transaction")
    public String showTransactionPage(@RequestParam("postId") Integer postId,
                                      Model model,
                                      Principal principal) {
        // Kiểm tra đối tượng principal
        if (principal == null) {
            throw new RuntimeException("Người dùng chưa đăng nhập!");
        }

        // Lấy thông tin người dùng từ service
        User user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new RuntimeException("Không tìm thấy user với username: " + principal.getName());
        }

        // Thêm thông tin vào model
        model.addAttribute("user", user);
        model.addAttribute("postId", postId);

        // Nếu file template nằm trong thư mục templates/transaction/transaction.html thì trả về "transaction/transaction"
        return "transaction/transaction";
    }

    @PostMapping("/pay")
    public String processPayment(@RequestParam("postId") Integer postId,
                                 @RequestParam("postType") String postType,
                                 @RequestParam("days") int days,
                                 Model model,
                                 Principal principal) {

        // Lấy thông tin người dùng hiện hành
        User user = userService.findUserByUsername(principal.getName());

        // Gọi service xử lý giao dịch
        Transaction transaction = transactionService.processTransaction(postId, postType, days, user);

        if ("SUCCESS".equalsIgnoreCase(transaction.getStatus())) {
            // Nếu thanh toán thành công, cập nhật bài post có postId chuyển payable thành "yes"
            Optional<Post> postOptional = postService.findById(postId);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                post.setPayable("yes");
                postService.save(post);  // Giả sử bạn có phương thức save để lưu lại bài post
            }
            // Có thể thêm thông báo giao dịch thành công nếu cần (ví dụ qua model hoặc session)
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Số dư trong tài khoản không đủ để thanh toán.");
            model.addAttribute("user", user);
            model.addAttribute("postId", postId);
            return "transaction/transaction";
        }
    }
}

