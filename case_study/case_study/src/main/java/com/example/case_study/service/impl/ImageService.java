package com.example.case_study.service.impl;

import com.example.case_study.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService implements IService {
    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Object save(Object o) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
