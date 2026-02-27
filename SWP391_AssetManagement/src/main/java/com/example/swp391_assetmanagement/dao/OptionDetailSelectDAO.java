package com.example.swp391_assetmanagement.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.time.LocalDateTime;

@Dao
@ConfigAutowireable
public interface OptionDetailSelectDAO {

    @Select
    long selectAssetExternalRequestDetailId(String OptionDetailId);

    @Select
    long selectAssetRequestId(String OptionDetailId);

    @Update(sqlFile = true)
    int updateRequestStatus(Long id, String status, Long approvedBy, LocalDateTime approvedDate);

    @Update(sqlFile = true)
    int updateExternalStatus(Long id, String status);

    @Update(sqlFile = true)
    int updateOptionSelection(Long id, String isSelected, Long approverBy, LocalDateTime approvedDate);
}
