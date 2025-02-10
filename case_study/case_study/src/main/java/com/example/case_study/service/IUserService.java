package com.example.case_study.service;

import com.example.case_study.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public interface IUserService extends IService<User> {
    User findUserByUsername(String username);

    void updateUser(User user);
    User save(User user);

    boolean existsByEmail(String email);
}
