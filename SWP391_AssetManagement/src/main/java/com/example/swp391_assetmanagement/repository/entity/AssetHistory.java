package com.example.swp391_assetmanagement.repository.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "assets_history")
public class AssetHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_id")
    public Long assetId;

    @Column(name = "asset_action_type_id")
    public Long assetActionTypeId;

    @Column(name = "from_user_id")
    public Long fromUserId;

    @Column(name = "to_user_id")
    public Long toUserId;

    @Column(name = "action_by")
    public Long actionBy;

    public String note;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
}

