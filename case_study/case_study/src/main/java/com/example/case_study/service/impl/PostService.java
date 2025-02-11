package com.example.case_study.service.impl;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.*;
import com.example.case_study.repository.ImageRepository;
import com.example.case_study.repository.PostRepository;
import com.example.case_study.repository.PurposeRepository;
import com.example.case_study.repository.RealEstateRepository;
import com.example.case_study.service.IPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.UUID;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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
        return postRepository.findByPayableOrderByPostTypeIdDesc("yes");
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
    public Post createPost(@Valid PostDTO postDTO, User user) {
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
            post.setPurpose(purpose);
            post.setRealEstate(realEstate);
            post.setUser(user);
            post.setPayable("no");

            // Xử lý lưu ảnh (như đã code)
            List<MultipartFile> files = postDTO.getImageFiles();
            String uploadDir = "uploads";
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
                post.setImage("/uploads/" + fileName);
            }
            post.setRealEstate(realEstate);
            // Lưu Post để có id nếu cần dùng cho Image sau này
            Post savedPost = postRepository.save(post);


            // Xử lý lưu các ảnh khác (nếu có)
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
            return savedPost;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xử lý file ảnh: " + e.getMessage());
        }
    }


    // Trong PostService.java
    @Override
    public void updatePost(Post post, @Valid PostDTO postDTO, List<Integer> deleteImageIds) {
        try {
            // 1. Cập nhật thông tin cơ bản của Post
            post.setTitle(postDTO.getTitle());
            post.setContent(postDTO.getContent());
            post.setStatus(postDTO.getStatus());
            post.setPublishDate(postDTO.getPublishDate());

            // 2. Cập nhật thông tin RealEstate
            RealEstate realEstate = post.getRealEstate();
            if (realEstate == null) {
                realEstate = new RealEstate();
            }
            realEstate.setType(postDTO.getType());
            realEstate.setLocation(postDTO.getLocation());
            realEstate.setDirection(postDTO.getDirection());
            realEstate.setArea(postDTO.getArea());
            realEstate.setPrice(postDTO.getPrice());
            realEstateRepository.save(realEstate);
            post.setRealEstate(realEstate);

            // 3. Cập nhật thông tin Purpose
            Purpose purpose = post.getPurpose();
            if (purpose == null) {
                purpose = new Purpose();
            }
            purpose.setPurpose(postDTO.getPurpose());
            purposeRepository.save(purpose);
            post.setPurpose(purpose);

            // 4. Xử lý file hình ảnh mới được upload (nếu có)
            List<MultipartFile> files = postDTO.getImageFiles();
            String uploadDir = "uploads";  // thư mục chứa file ảnh
            if (files != null && !files.isEmpty()) {
                // Nếu file đầu tiên (ảnh chính) có nội dung
                if (!files.get(0).isEmpty()) {
                    // Xóa ảnh chính cũ nếu có
                    if (post.getImage() != null) {
                        deleteImageFile(post.getImage());
                    }
                    MultipartFile mainFile = files.get(0);
                    String fileName = UUID.randomUUID().toString() + "_" + mainFile.getOriginalFilename();
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(mainFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    post.setImage("/uploads/" + fileName);

                    // (Tùy chọn) Lưu record của ảnh chính vào bảng Image
                    Image mainImage = new Image();
                    mainImage.setPost(post);
                    mainImage.setImage("/uploads/" + fileName);
                    imageRepository.save(mainImage);
                }
                // Xử lý các ảnh phụ (nếu có)
                for (int i = 1; i < files.size(); i++) {
                    MultipartFile file = files.get(i);
                    if (!file.isEmpty()) {
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        Path uploadPath = Paths.get(uploadDir);
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                        Image image = new Image();
                        image.setPost(post);
                        image.setImage("/uploads/" + fileName);
                        imageRepository.save(image);
                    }
                }
            }

            // 5. Xử lý xóa các ảnh được chọn (deleteImageIds truyền từ form)
            if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
                for (Integer imageId : deleteImageIds) {
                    Optional<Image> imageOpt = imageRepository.findById(imageId);
                    if (imageOpt.isPresent()) {
                        Image image = imageOpt.get();
                        deleteImageFile(image.getImage());
                        imageRepository.delete(image);
                    }
                }
            }

            // 6. Lưu lại thông tin Post đã cập nhật
            postRepository.save(post);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xử lý file ảnh: " + e.getMessage());
        }
    }



    public void deleteImageFile(String imagePath) {
        // Đường dẫn thư mục lưu file (nếu được cấu hình khác, bạn có thể sử dụng @Value để inject giá trị)
        String uploadDir = "uploads";
        // Lấy tên file từ đường dẫn
        Path filePath = Paths.get(uploadDir).resolve(Paths.get(imagePath).getFileName());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa file ảnh: " + e.getMessage());
        }
    }

    @Override
    public List<Post> getApprovedPosts() {
        return postRepository.findByStatus("Approved");
    }

    @Override
    public List<Post> getDraftPosts() {
        return postRepository.findByStatus("Pending");
    }

    @Override
    public List<Post> getApprovedPostsByUserId(Integer userId) {
        return postRepository.findByUserIdAndStatus(userId, "Approved");
    }


    @Override
    public List<Post> getDraftPostsByUserId(Integer userId) {
        // Trả về các bài viết của user có payable = "no"
        return postRepository.findByUserIdAndPayable(userId, "no");
    }



    @Override
    public List<PostDTO> searchPosts(String location, String type, String price, String area) {
        List<Post> posts = postRepository.searchPosts(location, type, price, area);

        return posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setStatus(post.getStatus());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setPublishDate(post.getPublishDate());
            dto.setPurpose(post.getPurpose() != null ? post.getPurpose().getPurpose() : null);
            dto.setLocation(post.getRealEstate().getLocation());
            dto.setArea(post.getRealEstate().getArea());
            dto.setDirection(post.getRealEstate().getDirection());
            dto.setPrice(post.getRealEstate().getPrice());
            dto.setImage(post.getImage());
            dto.setImages(post.getImages());
            dto.setPayable(post.getPayable());
            return dto;
        }).toList();
    }

    @Override
    public List<PostDTO> getListDefault() {
        List<Post> posts = postRepository.findLatestPosts(PageRequest.of(0, 4));


        return posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setStatus(post.getStatus());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setPublishDate(post.getPublishDate());
            dto.setPurpose(post.getPurpose() != null ? post.getPurpose().getPurpose() : null);
            dto.setLocation(post.getRealEstate().getLocation());
            dto.setArea(post.getRealEstate().getArea());
            dto.setDirection(post.getRealEstate().getDirection());
            dto.setPrice(post.getRealEstate().getPrice());
            dto.setImage(post.getImage());
            dto.setImages(post.getImages());
            dto.setPayable(post.getPayable());
            return dto;
        }).toList();
    }


}
