package com.example.case_study.service.impl;

import com.example.case_study.model.Deposit;
import com.example.case_study.repository.DepositRepository;
import com.example.case_study.service.IDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositService implements IDepositService {
    @Autowired
    private DepositRepository depositRepository;

    @Override
    public void saveDeposit(Deposit deposit) {
        depositRepository.save(deposit);
    }
}
