package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.servicerequest.OptionDetailListRequest;

import java.util.List;
import java.util.Optional;

public interface OptionDetailService {

    Optional<OptionDetail> getById(Long id);

    void create(OptionDetail plan);

    void update(OptionDetail plan);

    void saveAll(List<OptionDetail> plans);

    List<OptionDetail> getByRequestDetailId(OptionDetailListRequest request);

    int countByRequestDetailId(OptionDetailListRequest request);

    void unselectByRequestDetailId(Long requestDetailId);

    void deleteById(Long id);

}

