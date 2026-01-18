package com.example.swp391_assetmanagement.repository.entity.operation;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "assets_history")
public class AssetsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_status_id")
    public Long assetStatusId;

    @Column(name = "asset_id")
    public Long assetId;

    @Column(name = "from_location_id")
    public Long fromLocationId;

    @Column(name = "to_location_id")
    public Long toLocationId;

    @Column(name = "action_by")
    public Long actionBy;

    @Column(name = "note")
    public String note;

    @Column(name = "created_at")
    public LocalDateTime createdAt;
}

