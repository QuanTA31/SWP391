package com.example.swp391_assetmanagement.repository.entity;

import lombok.Setter;
import org.seasar.doma.*;

@Setter
@Entity
@Table(name = "request_type")
public class RequestType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
}

