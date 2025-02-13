package com.example.case_study.service.impl;

import com.example.case_study.dto.TransactionHistoryDto;
import com.example.case_study.model.Post;
import com.example.case_study.model.Transaction;
import com.example.case_study.model.User;
import com.example.case_study.repository.PostRepository;
import com.example.case_study.repository.TransactionRepository;
import com.example.case_study.service.ITransactionService;
import com.example.case_study.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private IUserService userService; // dùng để cập nhật số dư người dùng

    @Override
    public Transaction processTransaction(Integer postId, String postType, int days, User user) {
        // Lấy bài đăng theo id
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            throw new RuntimeException("Không tìm thấy bài đăng có id: " + postId);
        }
        Post post = optionalPost.get();

        // Tính toán số tiền cần thanh toán theo loại bài đăng và số ngày duy trì
        double basePrice = 0;
        switch (postType) {
            case "STANDARD":
                basePrice = 4000;
                break;
            case "VIP_SILVER":
                basePrice = 30000;
                break;
            case "VIP_GOLD":
                basePrice = 120000;
                break;
            case "VIP_DIAMOND":
                basePrice = 300000;
                break;
            default:
                throw new RuntimeException("Loại bài đăng không hợp lệ: " + postType);
        }
        double total = basePrice * days;
        if (days == 20) {
            total = total - total * 0.05;
        } else if (days == 30) {
            total = total - total * 0.10;
        }

        // Tạo đối tượng Transaction mới
        Transaction transaction = new Transaction();
        transaction.setPost(post);
        transaction.setPrice(total);

        // Kiểm tra số dư người dùng
        BigDecimal balance = user.getBalance();
        BigDecimal totalBig = BigDecimal.valueOf(total);
        if (balance.compareTo(totalBig) >= 0) {
            // Nếu số dư đủ:
            // - Trừ tiền từ số dư người dùng
            // - Cập nhật trạng thái payable của bài đăng thành "yes"
            // - Đánh dấu giao dịch là SUCCESS
            user.setBalance(balance.subtract(totalBig));
            userService.save(user); // cập nhật số dư cho người dùng

            post.setPayable("yes");
            postRepository.save(post);

            transaction.setStatus("SUCCESS");
        } else {
            // Nếu không đủ số dư thì giao dịch thất bại
            transaction.setStatus("FAILED");
        }

        // Lưu lại giao dịch vào CSDL
        return transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionHistoryDto> getAllTransactionHistory() {
        return null;
    }

    @Override
    public List<TransactionHistoryDto> getAllTransactionHistoryAllUser() {
        return transactionRepository.getAllTransactionHistoryAllUser();
    }
}
