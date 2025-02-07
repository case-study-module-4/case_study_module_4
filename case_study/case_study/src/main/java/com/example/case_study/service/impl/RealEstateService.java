package com.example.case_study.service.impl;

import com.example.case_study.dto.SearchDto;
import com.example.case_study.model.RealEstate;
import com.example.case_study.repository.RealEstateRepository;
import com.example.case_study.service.IRealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    @Override
    public List<RealEstate> searchRealEstate(SearchDto searchDto) {
        String locationSearch = null;
        if (searchDto.getType().trim().isEmpty()) {
            searchDto.setType(null);}
        if (searchDto.getLocation().trim().isEmpty()) {
            searchDto.setLocation(null);
        } else {
            locationSearch = "%" + searchDto.getLocation() + "%";
        }

        if (isAllFieldsNull(searchDto)) {
            return new ArrayList<>();
        } else {
            return realEstateRepository.searchRealEstate(locationSearch,
                    searchDto.getType(),
                    searchDto.getPrice(),
                    searchDto.getArea());
        }
    }

    private boolean isAllFieldsNull(Object obj) {
        if (obj == null) return true; // Nếu bản thân obj là null thì coi như toàn bộ null
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true); // Cho phép truy cập vào các trường private
                if (field.get(obj) != null) {
                    return false; // Chỉ cần 1 trường không null thì trả về false
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true; // Nếu duyệt hết mà không có trường nào khác null thì trả về true
    }
}
