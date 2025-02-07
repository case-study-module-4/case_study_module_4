package com.example.case_study.service;

import com.example.case_study.model.Account;

public interface IAccountService {
    Account findAccountByUsername(String username);
    void updateAccount(String username, String newPass);
    void saveAccount(Account account);
    boolean checkAccount(String username);
}
