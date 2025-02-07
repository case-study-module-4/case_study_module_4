package com.example.case_study.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private String status;

    @NotBlank(message = "Title must not be empty")
    @Size(max = 255, message = "Title must be at most 255 characters")
    @Column(name = "title", columnDefinition = "VARCHAR(255)", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_type_id", columnDefinition = "INT")
    private PostType postType;

    @ManyToOne
    @JoinColumn(name = "purpose_id", columnDefinition = "INT")
    private Purpose purpose;

    @ManyToOne
    @JoinColumn(name = "real_estate_id", columnDefinition = "INT")
    private RealEstate realEstate;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "INT")
    private User user;

    @NotNull(message = "Publish Date must not be null")
    private LocalDate publishDate;


}