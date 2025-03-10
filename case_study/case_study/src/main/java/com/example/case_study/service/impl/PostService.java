package com.example.case_study.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.case_study.config.CloudinaryConfig;
import com.example.case_study.dto.PostDTO;
import com.example.case_study.model.Image;
import com.example.case_study.model.Post;
import com.example.case_study.model.Purpose;
import com.example.case_study.model.RealEstate;
import com.example.case_study.model.User;
import com.example.case_study.repository.ImageRepository;
import com.example.case_study.repository.PostRepository;
import com.example.case_study.repository.PurposeRepository;
import com.example.case_study.repository.RealEstateRepository;
import com.example.case_study.service.IPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    private Cloudinary cloudinary = CloudinaryConfig.getCloudinary();


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
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setDeleted(true);
            postRepository.save(post);
        } else {
            throw new RuntimeException("Không tìm thấy bài đăng với id: " + id);
        }
    }

    @Override
    public Post createPost(@Valid PostDTO postDTO, User user) {
        try {
            // 1. Tạo đối tượng RealEstate từ PostDTO
            RealEstate realEstate = new RealEstate();
            realEstate.setType(postDTO.getType());
            realEstate.setLocation(postDTO.getLocation());
            realEstate.setDirection(postDTO.getDirection());
            realEstate.setArea(postDTO.getArea());
            realEstate.setPrice(postDTO.getPrice());
            realEstateRepository.save(realEstate);

            // 2. Tạo đối tượng Purpose từ DTO
            Purpose purpose = new Purpose();
            purpose.setPurpose(postDTO.getPurpose());
            purposeRepository.save(purpose);

            // 3. Tạo đối tượng Post
            Post post = new Post();
            post.setTitle(postDTO.getTitle());
            post.setContent(postDTO.getContent());
            post.setStatus(postDTO.getStatus() != null ? postDTO.getStatus() : "Pending");
            post.setPublishDate(postDTO.getPublishDate() != null ? postDTO.getPublishDate() : LocalDate.now());
            post.setPurpose(purpose);
            post.setRealEstate(realEstate);
            post.setUser(user);
            post.setPayable("no");

            // 4. Xử lý upload ảnh qua Cloudinary
            List<MultipartFile> files = postDTO.getImageFiles();
            if (files != null && !files.isEmpty() && !files.get(0).isEmpty()) {
                MultipartFile mainFile = files.get(0);
                Map<?, ?> uploadResult = cloudinary.uploader().upload(mainFile.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("secure_url").toString();
                post.setImage(imageUrl);
            }

            // 5. Lưu Post để có id nếu cần dùng cho các bản ghi ảnh sau này
            Post savedPost = postRepository.save(post);

            // 6. Upload và lưu các ảnh phụ (bỏ qua ảnh chính)
            if (files != null && files.size() > 1) {
                for (int i = 1; i < files.size(); i++) {
                    MultipartFile file = files.get(i);
                    if (!file.isEmpty()) {
                        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                        String imageUrl = uploadResult.get("secure_url").toString();
                        Image image = new Image();
                        image.setPost(post);
                        image.setImage(imageUrl);
                        imageRepository.save(image);
                    }
                }
            }
            return savedPost;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi upload ảnh lên Cloudinary: " + e.getMessage());
        }
    }

    @Override
    public void updatePost(Post post, @Valid PostDTO postDTO, List<Integer> deleteImageIds) {
        try {
            // 1. Cập nhật thông tin cơ bản của Post
            post.setTitle(postDTO.getTitle());
            post.setContent(postDTO.getContent());
            post.setStatus(postDTO.getStatus());
            if ("no".equalsIgnoreCase(post.getPayable())) {
                post.setPublishDate(postDTO.getPublishDate());
            }

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

            // 4. Xử lý upload ảnh mới (nếu có) qua Cloudinary
            List<MultipartFile> files = postDTO.getImageFiles();
            if (files != null && !files.isEmpty()) {
                if (!files.get(0).isEmpty()) {
                    MultipartFile mainFile = files.get(0);
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(mainFile.getBytes(), ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("secure_url").toString();
                    post.setImage(imageUrl);
                    Image mainImage = new Image();
                    mainImage.setPost(post);
                    mainImage.setImage(imageUrl);
                    imageRepository.save(mainImage);
                }
                if (files.size() > 1) {
                    for (int i = 1; i < files.size(); i++) {
                        MultipartFile file = files.get(i);
                        if (!file.isEmpty()) {
                            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                            String imageUrl = uploadResult.get("secure_url").toString();
                            Image image = new Image();
                            image.setPost(post);
                            image.setImage(imageUrl);
                            imageRepository.save(image);
                        }
                    }
                }
            }

            // 5. Xử lý xoá các ảnh được chọn
            if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
                for (Integer imageId : deleteImageIds) {
                    Optional<Image> imageOpt = imageRepository.findById(imageId);
                    if (imageOpt.isPresent()) {
                        Image image = imageOpt.get();
                        // Nếu cần, thêm logic xoá ảnh khỏi Cloudinary
                        imageRepository.delete(image);
                    }
                }
            }
            postRepository.save(post);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi upload ảnh lên Cloudinary: " + e.getMessage());
        }
    }

    @Override
    public List<Post> getApprovedPosts() {
        // Giả sử status "Approved" chỉ dành cho các bài đăng hợp lệ và chưa xóa
        return postRepository.findByStatusAndDeletedFalse("Approved");
    }

    @Override
    public List<Post> getDraftPosts() {
        return postRepository.findByStatusAndDeletedFalse("Pending");
    }

    @Override
    public List<Post> getApprovedPostsByUserId(Integer userId) {
        return postRepository.findByUserIdAndStatusAndDeletedFalse(userId, "yes");
    }

    @Override
    public List<Post> getDraftPostsByUserId(Integer userId) {
        return postRepository.findByUserIdAndPayableAndDeletedFalse(userId, "no");
    }

    @Override
    public List<PostDTO> searchPosts(String location, String type, String price, String area) {
        List<Post> posts = postRepository.searchPosts(location, type, price, area);
        return posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setStatus(post.getStatus());
            dto.setTitle(post.getTitle());
            dto.setType(post.getRealEstate().getType());
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
        List<Post> posts = postRepository.findLatestPosts(PageRequest.of(0, 8));
        return posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setStatus(post.getStatus());
            dto.setTitle(post.getTitle());
            dto.setType(post.getRealEstate().getType());
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
    public Long countByUserId(int userId) {
        return postRepository.countByUserIdAndDeletedFalse(userId);
    }

    @Override
    public List<Post> getAllPostsWithUsers() {
        return postRepository.findAllPostsWithUsers();
    }
}
