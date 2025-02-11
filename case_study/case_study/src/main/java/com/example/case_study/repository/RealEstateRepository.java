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
            "(:location IS NULL OR r.location ilike :location) AND " +
            "(r.type = :type OR :type IS NULL) AND " +
            "((:price IS NULL) OR " +
            "(:price = 0 AND CAST(r.price AS double) < 1000000000) OR " +
            "(:price = 1 AND CAST(r.price AS double) BETWEEN 1000000000 AND 3000000000) OR (:price = 2 AND CAST(r.price AS double) > 3000000000)) AND " +
            "((:area = 0 AND r.area < 50) OR (:area = 1 AND r.area BETWEEN 50 AND 100) OR (:area = 2 AND r.area > 100) OR :area IS NULL)"
    )
    List<RealEstate> searchRealEstate(@Param("location") String location,
                                      @Param("type") String type,
                                      @Param("price") Short price,
                                      @Param("area") Short area);




//    @Query("SELECT r FROM real_estate r WHERE " +
//            "( r.location ilike :location) AND " +
//            "(r.type = :type ) AND " +
//            "( " +
//            "(:price = 0 AND CAST(r.price AS double) < 1000000000) OR " +
//            "(:price = 1 AND CAST(r.price AS double) BETWEEN 1000000000 AND 3000000000) OR (:price = 2 AND CAST(r.price AS double) > 3000000000)) AND " +
//            "((:area = 0 AND r.area < 50) OR (:area = 1 AND r.area BETWEEN 50 AND 100) )"
//    )
//    List<RealEstate> searchRealEstate(@Param("location") String location,
//                                      @Param("type") String type,
//                                      @Param("price") Short price,
//                                      @Param("area") Short area);


}
