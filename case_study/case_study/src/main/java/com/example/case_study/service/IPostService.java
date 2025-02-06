package com.example.case_study.service;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;

import java.util.List;

public interface IPostService extends IService<Post> {

    List<Post> getApprovedPosts();

    List<Post> getDraftPosts();

    List<Post> getApprovedPostsByUserId(Integer userId);

    List<Post> getDraftPostsByUserId(Integer userId);
}
