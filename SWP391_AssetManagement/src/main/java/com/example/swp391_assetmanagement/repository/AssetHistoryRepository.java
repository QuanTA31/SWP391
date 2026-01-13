package com.example.swp391_assetmanagement.repository;

import com.example.swp391_assetmanagement.repository.entity.AssetHistory;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;
import java.util.Optional;

@Dao
@ConfigAutowireable
public interface AssetHistoryRepository {

    @Select
    Optional<AssetHistory> findById(Long id);

    @Insert
    int insert(AssetHistory assetHistory);

    @Update
    int update(AssetHistory assetHistory);

    @Delete
    int delete(AssetHistory assetHistory);

    @BatchInsert
    int[] batchInsert(List<AssetHistory> assetHistories);

    @BatchDelete
    int[] batchDelete(List<AssetHistory> assetHistories);
}
