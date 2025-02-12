package com.example.case_study.repository;

import com.example.case_study.dto.TransactionHistoryDto;
import com.example.case_study.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
//    @Query("SELECT new com.example.case_study.dto.TransactionHistoryDto( " +
//            "t.id, d.amount, d.paymentDate, d.paymentMethod) " +
//            "FROM transaction t " +
//            "JOIN t.post p " +
//            "JOIN p.user u " +
//            "LEFT JOIN deposit d ON u.id = d.user.id " +
//            "WHERE u.isDelete = false " +
//            "ORDER BY d.paymentDate DESC")
//    List<TransactionHistoryDto> getTransactionHistory();



}
