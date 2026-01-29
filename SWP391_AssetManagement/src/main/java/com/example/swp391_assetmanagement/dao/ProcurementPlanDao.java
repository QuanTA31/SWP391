package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;


import java.util.List;
import java.util.Optional;

@Dao
@ConfigAutowireable
public interface ProcurementPlanDao {

    @Select
    Optional<OptionDetail> findById(Long id);

    @Insert(sqlFile = true)
    int insert(OptionDetail optionDetail);

    @Update(sqlFile = true)
    int update(OptionDetail optionDetail);

    @Select
    List<OptionDetail> getByProcessId(Long processId);

    @Delete(sqlFile = true)
    int deleteById(Long id);

}
