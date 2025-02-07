package com.example.case_study.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {
    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotBlank(message = "Type cannot be blank")
    private String type;

    @NotNull(message = "Price cannot be null")
//    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Short price;

    @NotNull(message = "Area cannot be null")
    @Min(value = 0, message = "Area must be greater than or equal to 0")
    private Short area;
}
