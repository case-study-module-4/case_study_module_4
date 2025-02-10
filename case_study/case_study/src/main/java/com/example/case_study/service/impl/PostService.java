package com.example.case_study.service.impl;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Image;
import com.example.case_study.model.Post;
import com.example.case_study.model.Purpose;
import com.example.case_study.model.RealEstate;
import com.example.case_study.repository.ImageRepository;
import com.example.case_study.repository.PostRepository;
import com.example.case_study.repository.PurposeRepository;
import com.example.case_study.repository.RealEstateRepository;
import com.example.case_study.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PostService implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private PurposeRepository purposeRepository;

    @Autowired
    private ImageRepository imageRepository;

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
        try {
            // Tạo đối tượng RealEstate từ PostDTO
            RealEstate realEstate = new RealEstate();
            realEstate.setType(postDTO.getType());
            realEstate.setLocation(postDTO.getLocation());
            realEstate.setDirection(postDTO.getDirection());
            realEstate.setPrice(postDTO.getPrice());

            // Lưu vào database trước khi tạo Image
            realEstateRepository.save(realEstate);

            if (postDTO.getImage() != null && !postDTO.getImage().isEmpty()) {
                Image image = new Image();

                // Thư mục lưu file
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = postDTO.getImage().getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(postDTO.getImage().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Gán đường dẫn và RealEstate cho Image
                image.setImageUrl("/uploads/" + fileName);
                image.setRealEstate(realEstate);  // GÁN realEstate TRƯỚC KHI LƯU
                imageRepository.save(image);
            }

            Purpose purpose = new Purpose();
            purpose.setPurpose(postDTO.getPurpose());
            purposeRepository.save(purpose);

            Post post = new Post();
            post.setTitle(postDTO.getTitle());
            post.setContent(postDTO.getContent());

            // Kiểm tra trạng thái
            if (postDTO.getStatus() == null || postDTO.getStatus().trim().isEmpty()) {
                post.setStatus("Pending");
            } else {
                post.setStatus(postDTO.getStatus());
            }

            // Kiểm tra ngày đăng
            if (postDTO.getPublishDate() == null) {
                post.setPublishDate(LocalDate.now());
            } else {
                post.setPublishDate(postDTO.getPublishDate());
            }

            postRepository.save(post);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lưu ảnh: " + e.getMessage());
        }
    }


    @Override
    public List<Post> getApprovedPosts() {
        return postRepository.findByStatus("Approved") ;
    }

    @Override
    public List<Post> getDraftPosts() {
        return postRepository.findByStatus("Pending") ;
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
