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
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Integer id;

    @NotBlank(message = "Username must not be empty")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    @Column(name = "username", columnDefinition = "VARCHAR(50)", unique = true)
    private String username;

    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    @Column(name = "phone", columnDefinition = "VARCHAR(15)")
    private String phone;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", columnDefinition = "VARCHAR(100)")
    private String email;

    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be zero or positive")
    @Digits(integer = 13, fraction = 2, message = "Balance must have at most 13 integer digits and 2 decimal places")
    @Column(name = "balance", columnDefinition = "DECIMAL(15,2)", nullable = false)
    private double balance;

    @Lob
    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Column(name = "is_delete", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDelete = false;
}
