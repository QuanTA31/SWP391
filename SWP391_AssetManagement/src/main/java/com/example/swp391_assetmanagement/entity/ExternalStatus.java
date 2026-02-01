package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Setter
@Entity
@Table(name = "external_status")
public class ExternalStatus {

    @Id
    @Column(name = "id")
    public String id;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;
}
