package com.example.case_study.service.impl;

import com.example.case_study.model.Account;
import com.example.case_study.model.User;
import com.example.case_study.repository.AccountRepository;
import com.example.case_study.repository.UserRepository;
import com.example.case_study.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }


    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
    @Override
    public User findUserByUsername(String username) {
        return accountRepository.findByUsername(username).get().getUser();
    }

    @Override
    public void updateUser(User user) {
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Họ và tên không được để trống.");
        }
        userRepository.save(user);
    }
}
