package com.example.case_study.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Integer id;

    @NotBlank(message = "Trạng thái không được để trống")
    private String status;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Ngày đăng không được để trống")
    private LocalDate publishDate = LocalDate.now();

    @NotNull(message = "Mục đích không được để trống")
    private String purpose;

    @NotBlank(message = "Loại không được để trống")
    private String type;

    @Size(max = 255, message = "Địa điểm không được vượt quá 255 ký tự")
    private String location;

    @Size(max = 50, message = "Hướng không được vượt quá 50 ký tự")
    private String direction;

    @NotNull(message = "Giá không được để trống")
    private Double price;


    private List<MultipartFile> imageFiles;

}
