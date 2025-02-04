package com.example.case_study.service;

import com.example.case_study.model.RealEstate;
import com.example.case_study.repository.PostRepository;
import com.example.case_study.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

    


}
