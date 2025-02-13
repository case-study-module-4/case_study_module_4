package com.example.case_study.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositHistoryDto {
    private Integer id;

    @NotNull(message = "Amount cannot be empty")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Transaction date cannot be empty")
    private LocalDate paymentDate;

    @NotBlank(message = "Payment method cannot be empty")
    @Size(max = 50, message = "Payment method must not exceed 50 characters")
    private String paymentMethod;
}
