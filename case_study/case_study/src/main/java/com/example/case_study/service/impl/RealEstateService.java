package com.example.case_study.service.impl;

import com.example.case_study.model.RealEstate;
import com.example.case_study.repository.RealEstateRepository;
import com.example.case_study.service.IRealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RealEstateService implements IRealEstateService {

    @Autowired
    private RealEstateRepository realEstateRepository;

    @Override
    public List<RealEstate> findAll() {
        return realEstateRepository.findAll();
    }

    @Override
    public Optional<RealEstate> findById(Integer id) {
        return realEstateRepository.findById(id);
    }

    @Override
    public RealEstate save(RealEstate realEstate) {
        return realEstateRepository.save(realEstate);
    }

    @Override
    public void deleteById(Integer id) {
        realEstateRepository.deleteById(id);
    }
}
