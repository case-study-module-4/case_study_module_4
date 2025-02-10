package com.example.case_study.repository;

import com.example.case_study.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name); // Đúng định dạng
}
