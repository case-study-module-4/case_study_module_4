package com.example.case_study.repository;

import com.example.case_study.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByStatus(String status);

    List<Post> findByUserIdAndStatus(Integer userId, String status);
    List<Post> findByPayableOrderByPostTypeIdDesc(String payable);

    List<Post> findByUserIdAndPayable(Integer userId, String no);



    @Query("SELECT p FROM post p " +
            "WHERE " +
            "(:location = '' OR p.realEstate.location ILIKE %:location%) AND " +
            "(:type = '' OR p.realEstate.type = :type ) AND "+
            "(:price = '' OR ( " +
            "   (:price = '0' AND p.realEstate.price < 1000000000) OR " +
            "   (:price = '1' AND p.realEstate.price BETWEEN 1000000000 AND 3000000000) OR " +
            "   (:price = '2' AND p.realEstate.price > 3000000000) " +
            ")) AND " +
            "(:area = '' OR ( " +
            "   (:area = '0' AND p.realEstate.area < 50) OR " +
            "   (:area = '1' AND p.realEstate.area BETWEEN 50 AND 100) OR " +
            "   (:area = '2' AND p.realEstate.area > 100) " +
            "))")
    List<Post> searchPosts(
            @Param("location") String location,
            @Param("type") String type,
            @Param("price") String price,
            @Param("area") String area
    );

    @Query("SELECT p FROM post p ORDER BY p.publishDate DESC ")
    List<Post> findLatestPosts(Pageable pageable);


}



