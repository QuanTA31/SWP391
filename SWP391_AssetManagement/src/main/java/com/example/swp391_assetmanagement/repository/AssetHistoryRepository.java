package com.example.swp391_assetmanagement.repository;

import com.example.swp391_assetmanagement.repository.entity.operation.AssetsHistory;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;
import java.util.Optional;

@Dao
@ConfigAutowireable
public interface AssetHistoryRepository {

    @Select
    Optional<AssetsHistory> findById(Long id);

    @Insert
    int insert(AssetsHistory assetHistory);

    @Update
    int update(AssetsHistory assetHistory);

    @Delete
    int delete(AssetsHistory assetHistory);

    @BatchInsert
    int[] batchInsert(List<AssetsHistory> assetHistories);

    @BatchDelete
    int[] batchDelete(List<AssetsHistory> assetHistories);
}
