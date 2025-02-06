package com.example.case_study.repository;

import com.example.case_study.model.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Integer> {
    @Query("SELECT r FROM real_estate r WHERE " +
            "(r.location = :location OR :location IS NULL) AND " +
            "(r.realEstateType = :realEstateType OR :realEstateType IS NULL) AND " +
            "(r.price <= :price OR :price IS NULL) AND " +
            "(r.area <= :area OR :area IS NULL)")
    List<RealEstate> searchRealEstate(@Param("location") String location,
                                      @Param("realEstateType") String realEstateType,
                                      @Param("price") Double price,
                                      @Param("area") Double area);
}
