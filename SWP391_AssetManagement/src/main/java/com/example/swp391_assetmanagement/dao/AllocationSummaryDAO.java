package com.example.swp391_assetmanagement.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Map;
import java.util.List;

@Dao
@ConfigAutowireable
public interface AllocationSummaryDAO {

    @Select
    List<Map<String, Object>> countRequestStatusByUser(Long userId);
}
