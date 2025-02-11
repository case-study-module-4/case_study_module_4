package com.example.case_study.service;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import com.example.case_study.model.User;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface IPostService extends IService<Post> {


    Post createPost(@Valid PostDTO postDTO, User user);

    // Trong PostService.java
    void updatePost(Post post, @Valid PostDTO postDTO, List<Integer> deleteImageIds);

    List<Post> getApprovedPosts();

    List<Post> getDraftPosts();

    List<Post> getApprovedPostsByUserId(Integer userId);

    List<Post> getDraftPostsByUserId(Integer userId);

    List<PostDTO> searchPosts(String location, String type, String price, String area);

    List<PostDTO> getListDefault();

}
