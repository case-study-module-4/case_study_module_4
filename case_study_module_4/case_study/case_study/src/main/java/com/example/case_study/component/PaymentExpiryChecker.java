package com.example.case_study.component;

import com.example.case_study.model.Post;
import com.example.case_study.repository.PostRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PaymentExpiryChecker {

    private final PostRepository postRepository;

    public PaymentExpiryChecker(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Chạy định kỳ, ví dụ mỗi ngày một lần vào nửa đêm (có thể điều chỉnh cron theo ý bạn)
    @Scheduled(cron = "0 0 0 * * *")
    public void checkExpiredPosts() {
        // Lấy các bài đăng có trạng thái payable là "yes"
        List<Post> paidPosts = postRepository.findByPayableOrderByPostTypeIdDesc("yes");
        LocalDate today = LocalDate.now();
        for (Post post : paidPosts) {
            if (post.getPaymentExpiryDate() != null && post.getPaymentExpiryDate().isBefore(today.plusDays(1))) {
                // Nếu ngày hết hạn đã qua, cập nhật lại trạng thái
                post.setPayable("no");
                post.setPaymentExpiryDate(null); // hoặc giữ lại để lưu lịch sử nếu cần
                postRepository.save(post);
            }
        }
    }
}
