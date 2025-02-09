package com.example.case_study.controller;

import com.example.case_study.model.Image;
import com.example.case_study.repository.ImageRepository;
import com.example.case_study.service.impl.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/posts/image")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PostService postService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Integer id) {
        Optional<Image> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            // Xóa file ảnh khỏi hệ thống file
            postService.deleteImageFile(image.getImage());
            // Xóa record trong DB
            imageRepository.delete(image);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ảnh không tồn tại");
        }
    }
}

