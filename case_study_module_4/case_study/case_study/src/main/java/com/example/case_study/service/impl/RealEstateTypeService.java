package com.example.case_study.service.impl;

import com.example.case_study.model.RealEstateType;
import com.example.case_study.repository.RealEstateTypeRepository;
import com.example.case_study.service.IRealEstateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RealEstateTypeService implements IRealEstateTypeService {

    @Autowired
    private RealEstateTypeRepository realEstateTypeRepository;

    @Override
    public List<RealEstateType> findAll() {
        return realEstateTypeRepository.findAll();
    }

    @Override
    public Optional<RealEstateType> findById(Integer id) {
        return realEstateTypeRepository.findById(id);
    }

    @Override
    public RealEstateType save(RealEstateType realEstateType) {
        return realEstateTypeRepository.save(realEstateType);
    }

    @Override
    public void deleteById(Integer id) {
        realEstateTypeRepository.deleteById(id);
    }
}
