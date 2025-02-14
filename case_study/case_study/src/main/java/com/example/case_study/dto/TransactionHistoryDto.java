package com.example.case_study.dto;

import com.example.case_study.model.User;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryDto {

    private Integer id;

    @NotNull
    private String fullName;

    @NotNull(message = "Price cannot be empty")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private double price;

    @NotNull(message = "Transaction date cannot be empty")
    private LocalDate publishDate;

    @NotBlank(message = "Title must not be empty")
    private String title;


    public TransactionHistoryDto(Integer id, double price, LocalDate publishDate, String title) {
        this.id = id;
        this.price = price;
        this.publishDate = publishDate;
        this.title = title;
    }

    public TransactionHistoryDto(Integer id,String fullName, double price, LocalDate publishDate, String title) {
        this.id = id;
        this.fullName = fullName;
        this.price = price;
        this.publishDate = publishDate;
        this.title = title;
    }

    public String getFormattedId() {
        return String.format("T-%05d", this.id);
    }
}
