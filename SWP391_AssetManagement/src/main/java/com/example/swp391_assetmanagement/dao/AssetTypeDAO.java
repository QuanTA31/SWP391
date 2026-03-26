package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetType;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;

@Dao
@ConfigAutowireable
public interface AssetTypeDAO {
    @Select
    Optional<AssetType> findById(String id);
}
