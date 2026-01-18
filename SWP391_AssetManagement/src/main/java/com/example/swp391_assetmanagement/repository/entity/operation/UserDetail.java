package com.example.swp391_assetmanagement.repository.entity.operation;

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

    @Column(name = "name")
    public String name;

    @Column(name = "phone")
    public String phone;

    @Column(name = "email")
    public String email;

    @Column(name = "date_of_birth")
    public LocalDate dateOfBirth;

    @Column(name = "created_at")
    public LocalDateTime createdAt;
}
