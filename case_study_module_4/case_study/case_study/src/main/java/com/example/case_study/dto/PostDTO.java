package com.example.case_study.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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

    @NotNull(message = "Mục đích không được để trống")
    @Column(name = "purpose", columnDefinition = "ENUM('SALE', 'RENT')", nullable = false)
    private String purpose;

    @NotBlank(message = "Loại không được để trống")
    @Column(
            name = "type",
            columnDefinition = "ENUM('House', 'Apartment', 'Land', 'Hotel', 'Building')",
            nullable = false
    )
    private String type;

    @Size(max = 255, message = "Địa điểm không được vượt quá 255 ký tự")
    @Column(name = "location", columnDefinition = "VARCHAR(255)")
    private String location;

    @Size(max = 50, message = "Hướng không được vượt quá 50 ký tự")
    @Column(name = "direction", columnDefinition = "VARCHAR(50)")
    private String direction;

    @NotNull(message = "Giá không được để trống")
    @Column(name = "price", columnDefinition = "DECIMAL(15,2)")
    private Double price;

    @NotBlank(message = "Hình ảnh không được để trống")
    private String image; // Sử dụng String thay cho List<String> để đơn giản hóa

    @NotNull(message = "Ngày đăng không được để trống")
    @DateTimeFormat(pattern = "mm-dd-yyyy")
    private LocalDate publishDate;
}
