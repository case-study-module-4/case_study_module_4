package com.example.case_study.service.impl;

import com.example.case_study.controller.AccountController;
import com.example.case_study.dto.AccountDTO;
import com.example.case_study.dto.AccountRegisterDTO;
import com.example.case_study.model.Account;
import com.example.case_study.model.Role;
import com.example.case_study.model.User;
import com.example.case_study.repository.AccountRepository;
import com.example.case_study.repository.RoleRepository;
import com.example.case_study.repository.UserRepository;
import com.example.case_study.service.IAccountService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.IllegalSelectQueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    @Autowired
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;

    @Override
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public Role getDefaultRole() {
        return roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Vai trò ROLE_USER không tồn tại!"));
    }

    @Transactional
    public String registerAccount(AccountRegisterDTO accountDTO, String confirmPassword) {
        if (!accountDTO.getPassword().equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp!";
        }

        if (existsByUsername(accountDTO.getUsername())) {
            return "Tên đăng nhập đã tồn tại!";
        }

        if (existsByEmail(accountDTO.getEmail())) {
            return "Email đã được sử dụng!";
        }

        try {
            // Tạo User
            User user = new User();
            user.setFullName(accountDTO.getFullName());
            user.setPhone(accountDTO.getPhone());
            user.setEmail(accountDTO.getEmail());
            user.setBalance(BigDecimal.ZERO);
            user = userRepository.save(user);

            // Tạo Account
            Account account = new Account();
            account.setUsername(accountDTO.getUsername());
            account.setPassword(encodePassword(accountDTO.getPassword()));
            account.setUser(user);
            account.setRole(getDefaultRole());

            accountRepository.save(account);
            return "Đăng ký thành công! Hãy đăng nhập.";
        } catch (Exception e) {
            return "Đã xảy ra lỗi khi đăng ký, vui lòng thử lại!";
        }
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
//        return accountRepository.findAllAccountDetails();
        List<Account> accounts = accountRepository.findAll();
        List<AccountDTO> result = new ArrayList<>();
        for (Account account : accounts) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setId(account.getId());
            accountDTO.setFullName(account.getUser().getFullName());
            accountDTO.setPhone(account.getUser().getPhone());
            accountDTO.setEmail(account.getUser().getEmail());
            accountDTO.setBalance(account.getUser().getBalance());
            accountDTO.setImage(account.getUser().getImage());
            accountDTO.setIsDelete(account.getIsDelete());
            accountDTO.setStatus(account.getStatus());


            long postCount = postService.countByUserId(account.getUser().getId());
            accountDTO.setPostCount(postCount);


            result.add(accountDTO);
        }

        return result;
    }

    @Transactional
    public void toggleAccountStatus(Integer id) {
        System.out.println("--------------------------------------------------------------------------");
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new EntityNotFoundException("Tài khoản không tồn tại");
        }

        System.out.println(account);
        if (account.getStatus().equals("active")) {
            account.setStatus("inactive");
        } else {
            account.setStatus("active");
        }
        accountRepository.save(account);
    }

    @Override
    public Account findByUsername(String name) {
        return accountRepository.findByUsername(name).orElse(null);

    }

    @Transactional
    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

}
