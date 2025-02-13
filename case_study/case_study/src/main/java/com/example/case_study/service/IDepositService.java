package com.example.case_study.service;

import com.example.case_study.dto.DepositHistoryDto;
import com.example.case_study.model.Deposit;

import java.util.List;

public interface IDepositService {
    void saveDeposit(Deposit deposit);
    List<DepositHistoryDto> getAllDepositHistory();
    List<DepositHistoryDto> getAllDepositHistoryAllUser();
}
