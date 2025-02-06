package com.example.case_study.service;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;

import java.util.List;

public interface IPostService extends IService<Post> {

    // Lấy danh sách bài đăng đã phê duyệt của user
    List<PostDTO> getApprovedPostsByUser(Integer userId);

    // Lấy danh sách tin nháp của user
    List<PostDTO> getDraftPostsByUser(Integer userId);

    // Lấy chi tiết bài đăng theo ID
    PostDTO getPostById(Integer id);
}
