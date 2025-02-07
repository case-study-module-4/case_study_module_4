package com.example.case_study.service;

import com.example.case_study.dto.SearchDto;
import com.example.case_study.model.RealEstate;

import java.util.List;

public interface IRealEstateService extends IService<RealEstate> {
    List<RealEstate> searchRealEstate(SearchDto searchDto);
}
