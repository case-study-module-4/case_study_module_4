package com.example.case_study.service;

import com.example.case_study.dto.AccountDTO;
import com.example.case_study.model.Account;

import java.util.List;

public interface IAccountService {
    Account findAccountByUsername(String username);
    void updateAccount(String username, String newPass);
    void saveAccount(Account account);
    boolean checkAccount(String username);
    List<AccountDTO> getAllAccounts();
    boolean softDeleteAccount(Integer id);
}
