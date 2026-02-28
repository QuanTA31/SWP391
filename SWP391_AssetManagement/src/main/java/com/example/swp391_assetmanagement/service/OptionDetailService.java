package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.dto.request.OptionDetailListDTORequest;

import java.util.List;
import java.util.Optional;

public interface OptionDetailService {

    Optional<OptionDetail> getById(Long id);

    void create(OptionDetail plan);

    void update(OptionDetail plan);

    int countByRequestDetailId(
            Long requestDetailId,
            Boolean isSelected
    );

    void unselectByRequestDetailId(Long requestDetailId);

    void deleteById(Long id);

    List<OptionDetail> getList(OptionDetailListDTORequest request);

    int count(OptionDetailListDTORequest request);

    boolean existsRequestDetail(Long requestDetailId);

    List<OptionDetail> getListByRequestDetailId(Long requestDetailId);

    int[] updateRejectAll(List<OptionDetail> optionDetails);

}
