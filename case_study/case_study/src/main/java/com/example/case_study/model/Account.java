package com.example.case_study.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Integer id;

    @NotBlank(message = "Username must not be empty")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    @Column(name = "username", columnDefinition = "VARCHAR(50)", unique = true)
    private String username;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", columnDefinition = "VARCHAR(100)")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_role", referencedColumnName = "id", nullable = false)
    private Role role;

    @Column(name = "status", columnDefinition = "ENUM('active', 'inactive') DEFAULT 'active'")
    private String status = "active";

    @Column(name = "is_delete", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDelete = false;

    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id", nullable = false)
    private User user;

    @PrePersist
    @PreUpdate
    public void updateStatus() {
        if (Boolean.TRUE.equals(isDelete)) {
            this.status = "inactive";
        }
    }
}
