package com.example.case_study.repository;

import com.example.case_study.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    // Tìm bài đăng theo user và trạng thái
    List<Post> findByUserIdAndStatus(Integer userId, String status);
}
