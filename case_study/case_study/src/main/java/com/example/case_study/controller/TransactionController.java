package com.example.case_study.controller;

import com.example.case_study.dto.DepositHistoryDto;
import com.example.case_study.dto.TransactionHistoryDto;
import com.example.case_study.model.Post;
import com.example.case_study.model.PostType;
import com.example.case_study.model.Transaction;
import com.example.case_study.model.User;
import com.example.case_study.repository.PostTypeRepository;
import com.example.case_study.service.IDepositService;
import com.example.case_study.service.ITransactionService;
import com.example.case_study.service.IUserService;
import com.example.case_study.service.impl.PostService;
import com.example.case_study.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
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

    @Autowired
    private PostTypeRepository postTypeRepository;

    @Autowired
    private IDepositService depositService;


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
        User user = userService.findUserByUsername(principal.getName());
        Transaction transaction = transactionService.processTransaction(postId, postType, days, user);

        if ("SUCCESS".equalsIgnoreCase(transaction.getStatus())) {
            Optional<Post> postOptional = postService.findById(postId);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                post.setPayable("yes");
                post.setPaymentExpiryDate(LocalDate.now().plusDays(days));

                // Lấy PostType từ CSDL
                PostType updatedPostType = postTypeRepository.findByTypeName(postType)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy PostType: " + postType));
                post.setPostType(updatedPostType);

                postService.save(post);
            }
            return "redirect:/posts/approved";
        } else {
            model.addAttribute("error", "Số dư trong tài khoản không đủ để thanh toán.");
            model.addAttribute("user", user);
            model.addAttribute("postId", postId);
            return "transaction/transaction";
        }
    }


    @GetMapping("/transaction-history")
    public String getTransactionHistory(Model model, Principal piPrincipal) {
        List<DepositHistoryDto> deposits = depositService.getAllDepositHistory(piPrincipal.getName());
        List<TransactionHistoryDto> payments = transactionService.getAllTransactionHistory(piPrincipal.getName());


        model.addAttribute("deposits", deposits);
        model.addAttribute("payments", payments);

        return "transaction/transaction-history";
    }
}

