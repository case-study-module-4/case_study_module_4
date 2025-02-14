package com.example.case_study.dto;

import com.example.case_study.model.User;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class DepositHistoryDto {
    private Integer id;

    @NotNull
    private String fullName;

    @NotNull(message = "Amount cannot be empty")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Transaction date cannot be empty")
    private LocalDate paymentDate;

    @NotBlank(message = "Payment method cannot be empty")
    @Size(max = 50, message = "Payment method must not exceed 50 characters")
    private String paymentMethod;



    public DepositHistoryDto(Integer id, BigDecimal amount, LocalDate paymentDate, String paymentMethod) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    public DepositHistoryDto(Integer id,String fullName, BigDecimal amount, LocalDate paymentDate, String paymentMethod) {
        this.id = id;
        this.fullName = fullName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;

    }
}