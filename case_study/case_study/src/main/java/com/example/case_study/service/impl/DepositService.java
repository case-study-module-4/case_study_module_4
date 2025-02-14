package com.example.case_study.service.impl;

import com.example.case_study.dto.DepositHistoryDto;
import com.example.case_study.model.Deposit;
import com.example.case_study.repository.DepositRepository;
import com.example.case_study.service.IDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositService implements IDepositService {
    @Autowired
    private DepositRepository depositRepository;


    @Override
    public void saveDeposit(Deposit deposit) {
        depositRepository.save(deposit);
    }

    @Override
    public List<DepositHistoryDto> getAllDepositHistory(String userName) {
        return depositRepository.getAllDepositHistory(userName);
    }

    @Override
    public List<DepositHistoryDto> getAllDepositHistoryAllUser() {
        return depositRepository.getAllDepositHistoryAllUser();
    }
}
