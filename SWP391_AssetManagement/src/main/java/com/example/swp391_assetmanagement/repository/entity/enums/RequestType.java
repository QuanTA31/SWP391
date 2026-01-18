package com.example.swp391_assetmanagement.repository.entity.enums;

import lombok.Setter;
import org.seasar.doma.*;

@Setter
@Entity
@Table(name = "request_type")
public class RequestType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;
}
