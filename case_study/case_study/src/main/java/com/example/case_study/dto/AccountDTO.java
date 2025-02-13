package com.example.case_study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Integer id;
    private String fullName;
    private String phone;
    private String email;
    private BigDecimal balance;
    private String status;
    private String image;
    private Boolean isDelete;
    private Long postCount;
}
