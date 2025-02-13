package com.example.case_study.repository;

import com.example.case_study.model.PostInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostInterestRepository extends JpaRepository<PostInterest, Integer> {
    boolean existsByPostIdAndAccountId(Integer postId, Integer accountId);

    @Query("SELECT pi FROM post_interest pi " +
            "JOIN FETCH pi.post p " +
            "JOIN FETCH pi.account a " +
            "JOIN FETCH a.user u " +
            "WHERE p.user.id = :userId AND a.user.id <> :userId")
    List<PostInterest> findByPostOwnerId(Integer userId);
}
