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
@Entity(name = "post_type")
public class PostType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")

    private Integer id;


    @NotNull(message = "Type name must not be null")
    @Column(name = "type_name", columnDefinition = "ENUM('STANDARD', 'VIP_SILVER', 'VIP_GOLD', 'VIP_DIAMOND')", nullable = false)
    private String typeName;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Column(name = "price", columnDefinition = "DOUBLE", nullable = false)
    private double price;
}
