package com.example.case_study.controller;

import com.example.case_study.dto.AccountDTO;
import com.example.case_study.dto.DepositHistoryDto;
import com.example.case_study.dto.TransactionHistoryDto;
import com.example.case_study.model.Post;
import com.example.case_study.model.User;
import com.example.case_study.repository.PostTypeRepository;
import com.example.case_study.service.IDepositService;
import com.example.case_study.service.ITransactionService;
import com.example.case_study.service.IUserService;
import com.example.case_study.service.impl.AccountService;
import com.example.case_study.service.impl.PostService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/account")
public class AdminController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private PostService postService;

    @Autowired
    private IDepositService depositService;

    @GetMapping
    public String getAllAccounts(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return "redirect:/error";
        }

        List<AccountDTO> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        model.addAttribute("userId", user.getId());
        model.addAttribute("user", user);
        return "admin/account-list";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleAccountStatus(@PathVariable Integer id) {
        accountService.toggleAccountStatus(id);
        return "redirect:/admin/account";
    }

    @GetMapping("/transaction-history")
    public String getTransactionHistoryAllUser(Model model) {
        List<DepositHistoryDto> deposits = depositService.getAllDepositHistoryAllUser();
        List<TransactionHistoryDto> payments = transactionService.getAllTransactionHistoryAllUser();

        model.addAttribute("deposits", deposits);
        model.addAttribute("payments", payments);

        return "admin/transaction-history";
    }

    @GetMapping("/drafts")
    public String getDraftPosts(Model model,Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return "redirect:/error";
        }
        List<Post> draftPosts = postService.getAllDraftPosts();
        model.addAttribute("userId", user.getId());
        model.addAttribute("user", user);
        model.addAttribute("posts", draftPosts);
        return "admin/drafts-posts";
    }

    @GetMapping("/approved")
    public String getApprovedPosts(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return "redirect:/error";
        }
        List<Post> approvedPosts = postService.findAll();
        model.addAttribute("userId", user.getId());
        model.addAttribute("user", user);
        model.addAttribute("posts", approvedPosts);
        return "admin/approved-posts";
    }

//    @PostMapping("/{id}/delete")
//    public String deletePost(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
//        Optional<Post> postOptional = postService.findById(id);
//        if (postOptional.isPresent()) {
//            Post post = postOptional.get();
//            User user = userService.findUserByUsername(principal.getName());
//            // Kiểm tra quyền: Nếu user không phải admin và cũng không phải là chủ sở hữu bài đăng thì không được xóa
//            if (!user.getAccount().getRole().getName().equalsIgnoreCase("ROLE_ADMIN")
//                    && (post.getUser() == null || !post.getUser().equals(user))) {
//                return "redirect:/403";
//            }
//            postService.deleteById(id);
//            if ("no".equalsIgnoreCase(post.getPayable())) {
//                redirectAttributes.addFlashAttribute("message", "Xóa bài đăng thành công!");
//                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
//                return "redirect:/admin/account/drafts?message=deleted";
//            } else {
//                redirectAttributes.addFlashAttribute("message", "Xóa bài đăng thành công!");
//                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
//                return "redirect:/admin/account/approved?message=deleted";
//            }
//        }
//        redirectAttributes.addFlashAttribute("message", "Xóa bài đăng không thành công!");
//        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
//        return "redirect:/posts?error=notfound";
//    }
}
