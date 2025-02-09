package com.example.case_study.repository;

import com.example.case_study.dto.AccountDTO;
import com.example.case_study.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);
    @Query("SELECT new com.example.case_study.dto.AccountDTO("
            + "a.id, u.fullName, u.phone, u.email, u.balance, u.image, "
            + "CASE WHEN a.isDelete = true THEN 'inactive' ELSE COALESCE(a.status, 'active') END, "
            + "a.isDelete, "
            + "COUNT(p.id)) "
            + "FROM account a "
            + "JOIN a.user u "
            + "JOIN a.role r "
            + "LEFT JOIN post p ON p.user.id = u.id "
            + "WHERE r.name = 'ROLE_USER' "
            + "GROUP BY a.id, u.fullName, u.phone, u.email, u.balance, u.image, a.isDelete, a.status")
    List<AccountDTO> findAllAccountDetails();
}
