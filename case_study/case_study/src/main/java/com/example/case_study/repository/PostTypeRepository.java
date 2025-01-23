package com.example.case_study.repository;

import com.example.case_study.model.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTypeRepository extends JpaRepository<PostType, Long> {
}
