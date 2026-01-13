package com.example.swp391_assetmanagement.repository.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "request_approval")
public class RequestApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "request_id")
    public Long requestId;

    @Column(name = "request_type_id")
    public Long requestTypeId;

    @Column(name = "approver_level_1")
    public Long approverLevel1;

    @Column(name = "approver_level_2")
    public Long approverLevel2;

    @Column(name = "approval_status_id")
    public Long approvalStatusId;

    @Column(name = "approved_level_1_at")
    public LocalDateTime approvedLevel1At;

    @Column(name = "approved_level_2_at")
    public LocalDateTime approvedLevel2At;

    public String note;
}

