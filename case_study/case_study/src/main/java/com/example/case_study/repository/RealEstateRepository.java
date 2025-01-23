package com.example.case_study.repository;

import com.example.case_study.model.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealEstateRepository extends JpaRepository<RealEstate, Integer> {
}
