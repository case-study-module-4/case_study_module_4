package com.example.case_study.model;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER) // Hoặc LAZY nếu bạn muốn tải chậm
    @JoinColumn(name = "post_type_id", referencedColumnName = "id")
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

    @Column(name = "image", columnDefinition = "LONGTEXT")
    private String image;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> images;

    @Column(name = "payable", columnDefinition = "VARCHAR(3) DEFAULT 'no'")
    private String payable = "no";

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Transaction> transactions;

    @Column(name = "payment_expiry_date")
    private LocalDate paymentExpiryDate;
}