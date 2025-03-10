package com.example.case_study.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "real_estate_id", referencedColumnName = "id", nullable = true)
    private RealEstate realEstate;

    @NotBlank(message = "Image URL must not be empty")
    @Column(name = "image_url", columnDefinition = "TEXT", nullable = false)
    private String image;
}

