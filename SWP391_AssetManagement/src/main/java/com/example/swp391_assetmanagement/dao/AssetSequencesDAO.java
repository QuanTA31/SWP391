package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetSequences;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface AssetSequencesDAO {

    @Select
    AssetSequences findByIdToUpdate(String assetType);

    @Update(sqlFile = true)
    int update(AssetSequences assetSequences);
}
