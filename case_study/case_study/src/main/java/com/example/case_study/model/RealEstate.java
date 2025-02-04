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
@Entity(name = "real_estate")
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Integer id;

    @NotBlank(message = "Status must not be empty")
    @Column(name = "status", columnDefinition = "ENUM('Available', 'Sold', 'Rented')", nullable = false)
    private String status;

    @Size(max = 255, message = "Location must be at most 255 characters")
    @Column(name = "location", columnDefinition = "VARCHAR(255)")
    private String location;

    @Size(max = 50, message = "Direction must be at most 50 characters")
    @Column(name = "direction", columnDefinition = "VARCHAR(50)")
    private String direction;

    @NotNull(message = "Price must not be null")
    @Column(name = "price", columnDefinition = "DECIMAL(15,2)")
    private Double price;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;
    @ManyToOne
    @JoinColumn(name = "real_estate_type_id", columnDefinition = "INT")
    private RealEstateType realEstateType;

    @Column(name = "is_delete", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDelete = false;
}