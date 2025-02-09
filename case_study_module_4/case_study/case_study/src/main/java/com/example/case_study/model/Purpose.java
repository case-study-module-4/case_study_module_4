package com.example.case_study.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "purpose")
public class Purpose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")

    private Integer id;


    @NotNull(message = "Purpose must not be null")
    @Column(name = "purpose", columnDefinition = "ENUM('SALE', 'RENT')", nullable = false)
    private String purpose;
}
