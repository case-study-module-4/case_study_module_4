package com.example.case_study.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Integer id;

    @Column(name = "name", columnDefinition = "VARCHAR(50)", unique = true, nullable = false)
    private String name;

    @Column(name = "is_delete", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDelete = false;
}
