package com.example.case_study.service;

import com.example.case_study.dto.AccountDTO;
import com.example.case_study.dto.AccountRegisterDTO;
import com.example.case_study.model.Account;
import com.example.case_study.model.Role;
import jakarta.validation.Valid;

import java.util.List;

public interface IAccountService {
    void register(Account account);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    String encodePassword(String rawPassword);
    Role getDefaultRole();
    String registerAccount(@Valid AccountRegisterDTO accountDTO, String confirmPassword);
    List<AccountDTO> getAllAccounts();
    void toggleAccountStatus(Integer id);
}
