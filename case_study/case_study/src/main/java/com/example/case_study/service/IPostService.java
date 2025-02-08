package com.example.case_study.service;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import com.example.case_study.model.User;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface IPostService extends IService<Post> {


    void createPost(@Valid PostDTO postDTO, User user);

    // Trong PostService.java
    void updatePost(Post post, @Valid PostDTO postDTO, List<Integer> deleteImageIds);

    List<Post> getApprovedPosts();

    List<Post> getDraftPosts();

    List<Post> getApprovedPostsByUserId(Integer userId);

    List<Post> getDraftPostsByUserId(Integer userId);
}
