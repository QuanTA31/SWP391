package com.example.swp391_assetmanagement.repository.entity.operation;

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

    @Column(name = "request_progress_management_id")
    public Long requestProgressManagementId;

    @Column(name = "is_viewed")
    public Boolean isViewed;
}
