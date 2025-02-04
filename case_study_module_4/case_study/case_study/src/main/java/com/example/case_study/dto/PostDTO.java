package com.example.case_study.dto;

import com.example.case_study.model.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Integer id;

    @NotBlank(message = "Status must not be empty")
    private String status;

    @NotBlank(message = "Title must not be empty")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    private String content;

    private Purpose purpose;

    private RealEstate realEstate;

    private User user;

    @NotNull(message = "Publish Date must not be null")
    private LocalDate publishDate;

    private LocalDate endDate;

    // Thêm danh sách hình ảnh
    private List<Image> images;

    // Constructor để ánh xạ từ Post entity sang PostDTO
    public PostDTO(Post post) {
        this.id = post.getId();
        this.status = post.getStatus();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.purpose = post.getPurpose();
        this.realEstate = post.getRealEstate();
        this.user = post.getUser();
        this.publishDate = post.getPublishDate();
        this.endDate = post.getEndDate();
        // Nếu Post có danh sách hình ảnh, bạn có thể thêm chúng vào đây
        // Ví dụ: this.images = post.getImages();
    }
}