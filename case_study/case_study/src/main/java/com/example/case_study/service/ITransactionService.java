package com.example.case_study.service;

import com.example.case_study.model.Transaction;
import com.example.case_study.model.User;

public interface ITransactionService {
    Transaction processTransaction(Integer postId, String postType, int days, User user);
}
