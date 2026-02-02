package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.OptionDetailDao;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.servicerequest.OptionDetailListRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OptionDetailServiceImpl implements OptionDetailService {

    private final OptionDetailDao optionDetailDao; // 👈 BẮT BUỘC

    public OptionDetailServiceImpl(OptionDetailDao optionDetailDao) {
        this.optionDetailDao = optionDetailDao; // 👈 GÁN Ở ĐÂY
    }

    @Override
    public Optional<OptionDetail> getById(Long id) {
        return optionDetailDao.findById(id);
    }

    @Override
    @Transactional
    public void saveAll(List<OptionDetail> plans) {
        for (OptionDetail plan : plans) {
            optionDetailDao.insert(plan);
        }
    }

    @Override
    @Transactional
    public void unselectByRequestDetailId(Long requestDetailId) {
        optionDetailDao.unselectByRequestDetailId(requestDetailId);
    }

    @Override
    @Transactional
    public void create(OptionDetail plan) {
        optionDetailDao.insert(plan);
    }

    @Override
    @Transactional
    public void update(OptionDetail plan) {
        optionDetailDao.update(plan);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        optionDetailDao.deleteById(id);
    }

    @Override
    public List<OptionDetail> getByRequestDetailId(
            Long requestDetailId,
            Boolean isSelected,
            int offset,
            int pageSize
    ) {
        return optionDetailDao.getByRequestDetailId(
                requestDetailId,
                isSelected,
                offset,
                pageSize
        );
    }

    @Override
    public int countByRequestDetailId(
            Long requestDetailId,
            Boolean isSelected
    ) {
        return optionDetailDao.countByRequestDetailId(
                requestDetailId,
                isSelected
        );
    }



}

