package com.example.case_study.repository;

import com.example.case_study.model.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostTypeRepository extends JpaRepository<PostType, Integer> {
    Optional<PostType> findByTypeName(String typeName);
}
