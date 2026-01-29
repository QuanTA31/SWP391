package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.ProcurementPlanDao;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.ProcurementPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProcurementPlanServiceImpl implements ProcurementPlanService {

    private final ProcurementPlanDao procurementPlanDao; // 👈 BẮT BUỘC

    public ProcurementPlanServiceImpl(ProcurementPlanDao procurementPlanDao) {
        this.procurementPlanDao = procurementPlanDao; // 👈 GÁN Ở ĐÂY
    }

    @Override
    public Optional<OptionDetail> getById(Long id) {
        return procurementPlanDao.findById(id);
    }

    @Override
    @Transactional
    public void saveAll(List<OptionDetail> plans) {
        for (OptionDetail plan : plans) {
            procurementPlanDao.insert(plan);
        }
    }

    @Override
    public List<OptionDetail> getByProcessId(Long processId) {
        return procurementPlanDao.getByProcessId(processId);
    }

    @Override
    @Transactional
    public void create(OptionDetail plan) {
        procurementPlanDao.insert(plan);
    }

    @Override
    @Transactional
    public void update(OptionDetail plan) {
        procurementPlanDao.update(plan);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        procurementPlanDao.deleteById(id);
    }


}
