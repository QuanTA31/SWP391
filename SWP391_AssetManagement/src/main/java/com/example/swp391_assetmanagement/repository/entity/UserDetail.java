package com.example.swp391_assetmanagement.repository.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "user_detail")
public class UserDetail {

    @Id
    @Column(name = "user_id")
    public Long userId;

    public String name;
    public String phone;
    public String email;

    @Column(name = "date_of_birth")
    public LocalDate dateOfBirth;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
}

