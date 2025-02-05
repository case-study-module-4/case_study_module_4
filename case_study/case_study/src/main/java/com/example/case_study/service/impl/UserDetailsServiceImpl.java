package com.example.case_study.service.impl;

import com.example.case_study.model.Account;
import com.example.case_study.repository.Account.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found in the database"));

        // Lấy quyền từ role của account
        GrantedAuthority authority = new SimpleGrantedAuthority(account.getRole().getName());
        List<GrantedAuthority> grantList = Collections.singletonList(authority);

        return new User(account.getUsername(), account.getPassword(), grantList);
    }
}
