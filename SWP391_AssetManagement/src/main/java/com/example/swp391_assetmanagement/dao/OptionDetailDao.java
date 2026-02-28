package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.serviceresponse.OptionDetailServiceResponse;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Dao
@ConfigAutowireable
public interface OptionDetailDao {

    @Select
    Optional<OptionDetail> findById(Long id);

    @Insert(sqlFile = true)
    int insert(OptionDetail optionDetail);

    @Update(sqlFile = true)
    int update(OptionDetail optionDetail);

    @Select
    List<OptionDetailServiceResponse> getByRequestDetailId(
            Long requestDetailId,
            Boolean isSelected,
            int offset,
            int pageSize
    );

    @Select
    int countByRequestDetailId(
            Long requestDetailId,
            Boolean isSelected
    );

    @Update(sqlFile = true)
    int unselectByRequestDetailId(Long requestDetailId);

    @Delete(sqlFile = true)
    int deleteById(Long id);

    @Select
    int existsRequestDetail(Long requestDetailId);

    @Select
    List<OptionDetail> selectByUpdate(Long assetExternalRequestDetailId);

    @BatchUpdate(sqlFile = true)
    int[] batchUpdate(List<OptionDetail> details);

    @Select
    Integer countByIdAndStatus(Long requestDetailId, Boolean status);
}
