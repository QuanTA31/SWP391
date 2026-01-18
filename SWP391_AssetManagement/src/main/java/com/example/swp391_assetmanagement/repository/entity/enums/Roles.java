package com.example.swp391_assetmanagement.repository.entity.enums;

import lombok.Setter;
import org.seasar.doma.*;

@Setter
@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;
}
