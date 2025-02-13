package com.example.case_study.repository;

import com.example.case_study.dto.DepositHistoryDto;
import com.example.case_study.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Integer> {

    @Query("SELECT new com.example.case_study.dto.DepositHistoryDto( " +
            "d.id, d.amount,  d.paymentDate, d.paymentMethod) " +
            "FROM deposit d " +
            "JOIN d.user u " +
            "WHERE u.isDelete = false " +
            "ORDER BY d.paymentDate DESC")
    List<DepositHistoryDto> getAllDepositHistory();
}
