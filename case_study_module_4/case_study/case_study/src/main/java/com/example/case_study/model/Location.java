package com.example.case_study.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "name", columnDefinition = "INT")
    private String name;

    @Column(name ="slug",nullable = false)
    private String slug;

    @Column(name = "type",nullable =false)
    private String type;

    @JsonProperty("name_with_type")
    @Column(name = "name_with_type", nullable = false)
    private String nameWithType;

    @Column(name = "code", nullable =false)
    private String code;
}