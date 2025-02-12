package com.example.case_study.repository;

import com.example.case_study.dto.AccountDTO;
import com.example.case_study.dto.AccountRegisterDTO;
import com.example.case_study.model.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT new com.example.case_study.dto.AccountRegisterDTO( " +
            "a.username, a.password, u.fullName, u.phone, u.email) " +
            "FROM account a " +
            "JOIN a.user u " +
            "WHERE a.username = :username")
    Optional<AccountRegisterDTO> getAccountWithUserDetails(@Param("username") String username);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE account a SET a.status = :status WHERE a.id = :id")
    void updateAccountStatus(@Param("id") Integer id, @Param("status") String status);
}
