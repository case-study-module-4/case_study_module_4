package com.example.case_study.repository;

import com.example.case_study.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    // Lấy bài đăng theo status và chưa bị xóa
    List<Post> findByStatusAndDeletedFalse(String status);

    // Lấy bài đăng theo userId và status, chỉ lấy bài chưa xóa
    List<Post> findByUserIdAndStatusAndDeletedFalse(Integer userId, String status);

    @Query("SELECT p FROM post p WHERE p.payable = :payable AND p.deleted = false ORDER BY p.postType.id DESC")
    List<Post> findByPayableOrderByPostTypeIdDesc(@Param("payable") String payable);

    // Lấy bài đăng theo userId và payable, chỉ lấy bài chưa bị xóa
    List<Post> findByUserIdAndPayableAndDeletedFalse(Integer userId, String payable);

    // Đếm bài đăng theo userId, chỉ tính những bài chưa bị xóa
    long countByUserIdAndDeletedFalse(int userId);

    @Query("SELECT p FROM post p " +
            "WHERE p.deleted = false AND (p.payable IS NOT NULL AND p.payable = 'YES') AND  " +
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

    @Query("SELECT p FROM post p WHERE p.deleted = false AND (p.payable IS NOT NULL AND p.payable = 'YES') ORDER BY p.publishDate DESC")
    List<Post> findLatestPosts(Pageable pageable);

    @Query("SELECT p FROM post p JOIN FETCH p.user")
    List<Post> findAllPostsWithUsers();
}
