package com.example.case_study.service.impl;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import com.example.case_study.repository.PostRepository;
import com.example.case_study.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> findById(Integer id) {
        return postRepository.findById(id);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }


    @Override
    public void createPost(PostDTO postDTO) {
        Post post = new Post();
        post.setStatus(postDTO.getStatus());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setPublishDate(postDTO.getPublishDate());

        // Xử lý hình ảnh nếu cần
        if (postDTO.getImage() != null && !postDTO.getImage().isEmpty()) {
            byte[] imageBytes = postDTO.getImage().getBytes();
        }

        postRepository.save(post);
    }
    }
