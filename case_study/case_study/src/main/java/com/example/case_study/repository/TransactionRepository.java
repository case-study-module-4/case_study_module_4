package com.example.case_study.repository;

import com.example.case_study.dto.TransactionHistoryDto;
import com.example.case_study.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
 @Query("SELECT new com.example.case_study.dto.TransactionHistoryDto( " +
         "p.id, t.price,  p.publishDate, p.title) " +
         "FROM transaction t " +
         "JOIN t.post p " +
         "JOIN p.user u " +
         "JOIN u. account a " +
         "WHERE a.username =:userName  " +
         "ORDER BY p.publishDate DESC")
 List<TransactionHistoryDto> getAllTransactionHistory(String userName);

    @Query("SELECT new com.example.case_study.dto.TransactionHistoryDto( " +
            "p.id, t.price,  p.publishDate, p.title) " +
            "FROM transaction t " +
            "LEFT JOIN t.post p " +
            "ORDER BY p.publishDate DESC")
    List<TransactionHistoryDto> getAllTransactionHistoryAllUser();
}
