package com.example.swp391_assetmanagement.repository.entity.operation;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "user_code")
    public String userCode;

    @Column(name = "username")
    public String username;

    @Column(name = "password")
    public String password;

    @Column(name = "role_id")
    public Long roleId;

    @Column(name = "user_status_id")
    public Long userStatusId;

    @Column(name = "created_at")
    public LocalDateTime createdAt;
}
