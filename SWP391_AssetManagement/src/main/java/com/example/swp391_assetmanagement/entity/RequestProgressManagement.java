package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "request_progress_management")
public class RequestProgressManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "request_parent_id")
    public Long requestParentId;

    @Column(name = "approval_status_id")
    public String approvalStatusId;

    @Column(name = "approver_by")
    public Long approverBy;

    @Column(name = "approved_at")
    public LocalDateTime approvedAt;

    @Column(name = "note")
    public String note;
}
