package com.example.case_study.service.impl;

import com.example.case_study.model.Account;
import com.example.case_study.model.Post;
import com.example.case_study.model.PostInterest;
import com.example.case_study.repository.PostInterestRepository;
import com.example.case_study.repository.PostRepository;
import com.example.case_study.service.IPostInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PostInterestService implements IPostInterestService {
    @Autowired
    private PostInterestRepository postInterestRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<PostInterest> findAll() {
        return List.of();
    }

    @Override
    public Optional<PostInterest> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public PostInterest save(PostInterest postInterest) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    //     Lưu thông tin khi người dùng nhấn vào bài đăng.
    @Override
    public void logPostClick(Integer postId, Account account) {
        // Kiểm tra xem người dùng đã quan tâm chưa
        boolean alreadyInterested = postInterestRepository.existsByPostIdAndAccountId(postId, account.getId());
        if (!alreadyInterested) {
            // Lấy bài đăng từ repository
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Bài đăng không tồn tại"));
            // Tạo mới bản ghi
            PostInterest interest = new PostInterest();
            interest.setPost(post);
            interest.setAccount(account);
            interest.setInterestTime(LocalDate.now());

            // Lưu vào database
            postInterestRepository.save(interest);
        }
    }

    @Override
    public List<PostInterest> getPostInterestsByOwnerId(Integer userId) {
        return postInterestRepository.findByPostOwnerId(userId);
    }

}
