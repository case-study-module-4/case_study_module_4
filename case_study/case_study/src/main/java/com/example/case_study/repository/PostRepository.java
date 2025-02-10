package com.example.case_study.repository;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByStatus(String status);

    List<Post> findByUserIdAndStatus(Integer userId, String status);
    List<Post> findByPayableOrderByPostTypeIdDesc(String payable);

    List<Post> findByUserIdAndPayable(Integer userId, String no);



//    @Query("SELECT p FROM post p WHERE " +
//            "(:location IS NULL OR p.realEstate.location LIKE %:location%) AND " +
//            "(:price IS NULL OR ( " +
//            "   (:price = '0' AND p.realEstate.price < 1000000000) OR " +
//            "   (:price = '1' AND p.realEstate.price BETWEEN 1000000000 AND 3000000000) OR " +
//            "   (:price = '2' AND p.realEstate.price > 3000000000) " +
//            ")) AND " +
//            "(:area IS NULL OR ( " +
//            "   (:area = '0' AND p.realEstate.area < 50) OR " +
//            "   (:area = '1' AND p.realEstate.area BETWEEN 50 AND 100) " +
//            "))")
//    List<Post> searchPosts(
//            @Param("location") String location,
//            @Param("type") String type,
//            @Param("price") String price,
//            @Param("area") String area
//    );


}



