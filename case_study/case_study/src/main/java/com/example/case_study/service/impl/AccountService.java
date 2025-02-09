package com.example.case_study.service.impl;

import com.example.case_study.dto.AccountDTO;
import com.example.case_study.model.Account;
import com.example.case_study.repository.AccountRepository;
import com.example.case_study.service.IAccountService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account findAccountByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void updateAccount(String username, String newPass) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        account.setPassword(passwordEncoder.encode(newPass));
        accountRepository.save(account);
    }

    @Transactional
    public void saveAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
    }

    public boolean checkAccount(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAllAccountDetails();
    }
    @Override
    public boolean softDeleteAccount(Integer id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setIsDelete(true);
            accountRepository.save(account); // Cập nhật vào database
            return true;
        }
        return false;
    }
}
