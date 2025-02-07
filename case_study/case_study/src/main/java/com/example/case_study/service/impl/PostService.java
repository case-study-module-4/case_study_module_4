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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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
    public void createPost(@Valid PostDTO postDTO) {
        try {
            // Tạo đối tượng RealEstate từ PostDTO
            RealEstate realEstate = new RealEstate();
            realEstate.setType(postDTO.getType());
            realEstate.setLocation(postDTO.getLocation());
            realEstate.setDirection(postDTO.getDirection());
            realEstate.setArea(postDTO.getArea());
            realEstate.setPrice(postDTO.getPrice());
            realEstateRepository.save(realEstate);

            // Tạo đối tượng Purpose từ DTO
            Purpose purpose = new Purpose();
            purpose.setPurpose(postDTO.getPurpose());
            purposeRepository.save(purpose);

            // Tạo đối tượng Post
            Post post = new Post();
            post.setTitle(postDTO.getTitle());
            post.setContent(postDTO.getContent());
            post.setStatus(postDTO.getStatus() != null ? postDTO.getStatus() : "Pending");
            post.setPublishDate(postDTO.getPublishDate() != null ? postDTO.getPublishDate() : LocalDate.now());

            // Lấy danh sách file ảnh được upload
            List<MultipartFile> files = postDTO.getImageFiles();
            // Đường dẫn thư mục lưu file (có thể lấy từ cấu hình)
            String uploadDir = "uploads"; // hoặc dùng @Value("${file.upload-dir}") để lấy từ application.properties

            if (files != null && !files.isEmpty() && !files.get(0).isEmpty()) {
                // Lưu ảnh đầu tiên làm ảnh chính
                MultipartFile mainFile = files.get(0);
                // Tạo tên file duy nhất để tránh trùng lặp
                String fileName = UUID.randomUUID().toString() + "_" + mainFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(mainFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                // Lưu đường dẫn file (URL tương đối) vào đối tượng Post
                post.setImage("/uploads/" + fileName);
            }
            post.setRealEstate(realEstate);
            // Lưu Post để có id nếu cần dùng cho Image sau này
            postRepository.save(post);

            // Xử lý lưu các ảnh khác (nếu có) vào bảng Image
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        Path uploadPath = Paths.get(uploadDir);
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                        Image image = new Image();
                        image.setPost(post); // liên kết ảnh với bài đăng
                        image.setImage("/uploads/" + fileName); // lưu đường dẫn ảnh
                        imageRepository.save(image);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xử lý file ảnh: " + e.getMessage());
        }
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
