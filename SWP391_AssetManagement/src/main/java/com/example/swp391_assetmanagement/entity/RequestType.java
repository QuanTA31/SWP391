package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.*;

@Setter
@Entity
@Table(name = "request_type")
public class RequestType {

    @Id
    @Column(name = "id")
    public String id;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;
}
