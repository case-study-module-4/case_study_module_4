package com.example.case_study.service;

import com.example.case_study.model.User;

public interface IUserService extends IService<User> {
    User findByUsername(String username);
}
