package com.example.case_study.service.implement;

import com.example.case_study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void updateUserBalance(Integer userId, BigDecimal amount) {
        userRepository.updateUserBalance(userId, amount);
    }
}
