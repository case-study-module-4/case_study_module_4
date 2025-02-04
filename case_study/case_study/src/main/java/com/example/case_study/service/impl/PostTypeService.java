package com.example.case_study.service.impl;

import com.example.case_study.model.PostType;
import com.example.case_study.repository.PostTypeRepository;
import com.example.case_study.service.IPostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostTypeService implements IPostTypeService {

    @Autowired
    private PostTypeRepository postTypeRepository;

    @Override
    public List<PostType> findAll() {
        return postTypeRepository.findAll(); // Trả về danh sách tất cả PostType
    }

    @Override
    public Optional<PostType> findById(Integer id) {
        return postTypeRepository.findById(id); // Trả về Optional<PostType>
    }

    @Override
    public PostType save(PostType postType) {
        return postTypeRepository.save(postType); // Lưu PostType và trả về đối tượng đã lưu
    }

    @Override
    public void deleteById(Integer id) {
        postTypeRepository.deleteById(id); // Xóa PostType theo ID
    }
}