package com.example.case_study.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryDto {

    private Integer id;

    @NotNull(message = "Price cannot be empty")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private double price;

    @NotNull(message = "Transaction date cannot be empty")
    private LocalDate publishDate;

    @NotBlank(message = "Title must not be empty")
    private String title;

    public String getFormattedId() {
        return String.format("T-%05d", this.id);
    }

}
