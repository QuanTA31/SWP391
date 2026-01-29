package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.OptionDetail;

import java.util.List;
import java.util.Optional;

public interface ProcurementPlanService {

    Optional<OptionDetail> getById(Long id);

    void create(OptionDetail plan);

    void update(OptionDetail plan);

    void saveAll(List<OptionDetail> plans);

    List<OptionDetail> getByProcessId(Long processId);

    void deleteById(Long id);

}
