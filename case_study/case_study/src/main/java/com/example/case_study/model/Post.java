package com.example.case_study.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Integer id;

    @NotBlank(message = "Status must not be empty")
    @Column(name = "status", columnDefinition = "ENUM('Pending', 'Approved', 'Rejected')", nullable = false)
    private String status;

    @NotBlank(message = "Title must not be empty")
    @Size(max = 255, message = "Title must be at most 255 characters")
    @Column(name = "title", columnDefinition = "VARCHAR(255)", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @NotNull(message = "Category ID must not be null")
    @Column(name = "category_id", columnDefinition = "INT")
    private int categoryId;

    @NotNull(message = "Purpose ID must not be null")
    @Column(name = "purpose_id", columnDefinition = "INT")
    private int purposeId;

    @NotNull(message = "Real Estate ID must not be null")
    @Column(name = "real_estate_id", columnDefinition = "INT")
    private int realEstateId;

    @NotNull(message = "User ID must not be null")
    @Column(name = "user_id", columnDefinition = "INT")
    private int userId;

    @NotNull(message = "Publish Date must not be null")
    @Column(name = "publish_date", columnDefinition = "DATE", nullable = false)
    private java.time.LocalDate publishDate;

    @Column(name = "end_date", columnDefinition = "DATE")
    private java.time.LocalDate endDate;
}
