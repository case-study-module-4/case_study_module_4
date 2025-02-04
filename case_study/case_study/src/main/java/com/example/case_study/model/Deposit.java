package com.example.case_study.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "deposit")
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Integer id;

    @NotNull(message = "User ID must not be null")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    @Digits(integer = 13, fraction = 2, message = "Amount must have at most 13 integer digits and 2 decimal places")
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Payment date must not be null")
    @PastOrPresent(message = "Payment date cannot be in the future")
    @Column(name = "payment_date", columnDefinition = "DATE", nullable = false)
    private LocalDate paymentDate;

    @NotBlank(message = "Payment method must not be empty")
    @Size(max = 50, message = "Payment method must not exceed 50 characters")
    @Column(name = "payment_method", columnDefinition = "VARCHAR(50)")
    private String paymentMethod;

    @NotNull(message = "Status must not be null")
    @Column(name = "status", columnDefinition = "ENUM('Pending', 'Completed', 'Cancelled')", nullable = false)
    private String status;
}
