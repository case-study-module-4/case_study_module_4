package com.example.case_study.service.impl;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import com.example.case_study.repository.PostRepository;
import com.example.case_study.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        // Copy các trường khác
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        // Các mapping cho các trường quan hệ (Purpose, RealEstate, …) cũng cần được xử lý ở đây

        // Kiểm tra và gán giá trị mặc định cho status
        if (postDTO.getStatus() == null || postDTO.getStatus().trim().isEmpty()) {
            post.setStatus("Pending");
        } else {
            post.setStatus(postDTO.getStatus());
        }

        // Kiểm tra và gán giá trị mặc định cho publishDate
        if (postDTO.getPublishDate() == null) {
            post.setPublishDate(LocalDate.now());
        } else {
            post.setPublishDate(postDTO.getPublishDate());
        }

        postRepository.save(post);
    }

    @Override
    public List<Post> getApprovedPostsByUserId(Integer userId) {
        return postRepository.findByUserIdAndStatus(userId, "Approved") ;
    }

    @Override
    public List<Post> getDraftPostsByUserId(Integer userId) {
        return postRepository.findByUserIdAndStatus(userId, "Pending") ;
    }
}
