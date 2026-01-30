package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.*;

@Setter
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "title")
    public String title;

    @Column(name = "assignee_to")
    public Long assigneeTo;

    @Column(name = "asset_request_id")
    public Long assetRequestId;

    @Column(name = "read_already")
    public Boolean readAlready;
}
