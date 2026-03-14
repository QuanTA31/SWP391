package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.UpdateUserStatusDAO;
import com.example.swp391_assetmanagement.entity.Users;
import com.example.swp391_assetmanagement.service.UpdateUserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserStatusImpl implements UpdateUserStatusService {

    private final UpdateUserStatusDAO updateUserStatusDAO;

    @Override
    public int UpdateStatus(Users users) {
        return updateUserStatusDAO.changeStatusByUsername(users);
    }
}
