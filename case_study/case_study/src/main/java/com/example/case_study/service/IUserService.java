package com.example.case_study.service;

import com.example.case_study.model.User;

import java.math.BigDecimal;

public interface IUserService extends IService<User> {
    void updateUserBalance(Integer userId, BigDecimal amount);
    User findUserByUsername(String username);
}
