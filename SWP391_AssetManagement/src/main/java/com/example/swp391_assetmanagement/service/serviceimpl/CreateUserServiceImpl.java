package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.CreateUserDAO;
import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.entity.Users;
import com.example.swp391_assetmanagement.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;




@Service
@RequiredArgsConstructor
public class CreateUserServiceImpl implements CreateUserService {

    private final CreateUserDAO createUserDAO;

    @Override
    public long countTotalUsers() {
        return createUserDAO.CountTotalUsers();
    }

    @Override
    public void saveUser(Users user, UserDetail detail) {
        createUserDAO.insertUser(user);

        detail.setUserId(user.id);
        createUserDAO.insertUserDetail(detail);
    }
}
