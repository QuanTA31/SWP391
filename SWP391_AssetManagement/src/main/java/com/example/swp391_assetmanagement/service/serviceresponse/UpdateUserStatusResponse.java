package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Getter
@Entity
public class UpdateUserStatusResponse {
    @Column(name = "status_id")
    public String statusId;
}
