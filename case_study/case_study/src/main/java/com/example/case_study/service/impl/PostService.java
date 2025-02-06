package com.example.case_study.service.impl;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Post;
import com.example.case_study.repository.PostRepository;
import com.example.case_study.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // Lấy danh sách bài đăng đã phê duyệt của user
    @Override
    public List<PostDTO> getApprovedPostsByUser(Integer userId) {
        List<Post> posts = postRepository.findByUserIdAndStatus(userId, "Approved");
        return posts.stream().map(post -> new PostDTO(post)).collect(Collectors.toList());
    }

    // Lấy danh sách tin nháp của user
    @Override
    public List<PostDTO> getDraftPostsByUser(Integer userId) {
        List<Post> posts = postRepository.findByUserIdAndStatus(userId,"Draft");
        return posts.stream().map(post -> new PostDTO(post)).collect(Collectors.toList());
    }

    // Lấy chi tiết bài đăng theo ID
    @Override
    public PostDTO getPostById(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(PostDTO::new).orElse(null);
    }
}
