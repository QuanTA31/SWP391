package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.servicerequest.OptionDetailListRequest;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;


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
    List<OptionDetail> getByRequestDetailId(OptionDetailListRequest request);

    @Select
    int countByRequestDetailId(OptionDetailListRequest request);

    @Update(sqlFile = true)
    int unselectByRequestDetailId(Long requestDetailId);

    @Delete(sqlFile = true)
    int deleteById(Long id);

}

