package com.example.case_study.service;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import jakarta.validation.Valid;


public interface IPostService extends IService<Post> {
    void createPost(@Valid PostDTO postDTO);
}
