package com.example.case_study.service.impl;

import com.example.case_study.model.Image;
import com.example.case_study.repository.ImageRepository;
import com.example.case_study.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService implements IImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<Image> findAll() {
        return imageRepository.findAll(); // Trả về danh sách tất cả Image
    }

    @Override
    public Optional<Image> findById(Integer id) {
        return imageRepository.findById(id); // Trả về Optional<Image>
    }

    @Override
    public Image save(Image image) {
        return imageRepository.save(image); // Lưu Image và trả về đối tượng đã lưu
    }

    @Override
    public void deleteById(Integer id) {
        imageRepository.deleteById(id); // Xóa Image theo ID
    }
}
