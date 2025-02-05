package com.example.case_study.repository;

import com.example.case_study.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE user u SET u.balance = u.balance + :amount WHERE u.id = :userId")
    void updateUserBalance(@Param("userId") Integer userId, @Param("amount") BigDecimal amount);
}
