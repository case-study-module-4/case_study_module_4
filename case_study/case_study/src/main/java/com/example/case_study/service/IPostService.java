package com.example.case_study.service;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface IPostService extends IService<Post> {
    void createPost(@Valid PostDTO postDTO);


    List<Post> getApprovedPosts();

    List<Post> getDraftPosts();

    List<Post> getApprovedPostsByUserId(Integer userId);

    List<Post> getDraftPostsByUserId(Integer userId);
}
