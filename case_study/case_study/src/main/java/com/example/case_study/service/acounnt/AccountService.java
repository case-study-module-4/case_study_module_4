package com.example.case_study.service.acounnt;

import com.example.case_study.dto.AccountRegisterDTO;
import com.example.case_study.model.Account;
import com.example.case_study.model.Role;
import com.example.case_study.model.User;
import com.example.case_study.repository.Account.AccountRepository;
import com.example.case_study.repository.RoleRepository;
import com.example.case_study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(Account account) {
        if (existsByUsername(account.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        if (existsByEmail(account.getUser().getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        account.setPassword(encodePassword(account.getPassword()));
        account.setRole(getDefaultRole());

        accountRepository.save(account);
    }

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
            user.setBalance(new BigDecimal(0.0));
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
}
