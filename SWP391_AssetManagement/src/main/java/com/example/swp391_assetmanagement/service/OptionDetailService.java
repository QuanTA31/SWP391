package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.dto.request.OptionDetailListRequest;

import java.util.List;
import java.util.Optional;

public interface OptionDetailService {

    Optional<OptionDetail> getById(Long id);

    void create(OptionDetail plan);

    void update(OptionDetail plan);

    void saveAll(List<OptionDetail> plans);

    List<OptionDetail> getByRequestDetailId(
            Long requestDetailId,
            Boolean isSelected,
            int offset,
            int pageSize
    );

    int countByRequestDetailId(
            Long requestDetailId,
            Boolean isSelected
    );

    void unselectByRequestDetailId(Long requestDetailId);

    void deleteById(Long id);

    List<OptionDetail> getList(OptionDetailListRequest request);

    int count(OptionDetailListRequest request);

    boolean existsRequestDetail(Long requestDetailId);

}
