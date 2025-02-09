package com.example.case_study.repository;

import com.example.case_study.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByStatus(String status);

    List<Post> findByUserIdAndStatus(Integer userId, String status);
    List<Post> findByPayableOrderByPostTypeIdDesc(String payable);

    List<Post> findByUserIdAndPayable(Integer userId, String no);

}
