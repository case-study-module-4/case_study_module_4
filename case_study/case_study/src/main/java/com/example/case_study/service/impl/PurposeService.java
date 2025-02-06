package com.example.case_study.service.impl;

import com.example.case_study.model.Purpose;
import com.example.case_study.repository.PurposeRepository;
import com.example.case_study.service.IPurposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurposeService implements IPurposeService {
    @Autowired
    private PurposeRepository purposeRepository;

    @Override
    public List<Purpose> findAll() {
        return purposeRepository.findAll();
    }

    @Override
    public Optional<Purpose> findById(Integer id) {
        return purposeRepository.findById(id);
    }

    @Override
    public Purpose save(Purpose purpose) {
        return purposeRepository.save(purpose);
    }

    @Override
    public void deleteById(Integer id) {
        purposeRepository.deleteById(id);
    }

}
