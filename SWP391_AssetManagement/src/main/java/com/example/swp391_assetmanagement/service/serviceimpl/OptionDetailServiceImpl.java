package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.OptionDetailDao;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.dto.request.OptionDetailListDTORequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OptionDetailServiceImpl implements OptionDetailService {

    private final OptionDetailDao optionDetailDao;

    public OptionDetailServiceImpl(OptionDetailDao optionDetailDao) {
        this.optionDetailDao = optionDetailDao;
    }

    @Override
    public Optional<OptionDetail> getById(Long id) {
        return optionDetailDao.findById(id);
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
    public int countByRequestDetailId(
            Long requestDetailId,
            Boolean isSelected
    ) {
        return optionDetailDao.countByRequestDetailId(
                requestDetailId,
                isSelected
        );
    }

    @Override
    public List<OptionDetail> getList(OptionDetailListDTORequest request) {
        return optionDetailDao.getByRequestDetailId(
                request.getRequestDetailId(),
                request.getIsSelected(),
                request.getOffset(),
                request.getPageSize()
        );
    }

    @Override
    public int count(OptionDetailListDTORequest request) {
        return optionDetailDao.countByRequestDetailId(
                request.getRequestDetailId(),
                request.getIsSelected()
        );
    }

    @Override
    public boolean existsRequestDetail(Long requestDetailId) {
        return optionDetailDao.existsRequestDetail(requestDetailId) > 0;
    }

    @Override
    public Integer countByIdAndStatus(Long id, Boolean status) {
        return optionDetailDao.countByIdAndStatus(id, status);
    }

}

