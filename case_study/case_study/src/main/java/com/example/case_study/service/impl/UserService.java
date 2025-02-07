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
import java.math.BigDecimal;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.empty();
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
    public void updateUserBalance(Integer userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setBalance(user.getBalance().add(amount));

        userRepository.save(user);
    }
}
