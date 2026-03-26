package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.CreateUserDAO;
import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.entity.Users;
import com.example.swp391_assetmanagement.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


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

        // 1. Check if Username already exists
        if (createUserDAO.existsByUsername(user.username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }

        // 2. Check if Email already exists
        if (createUserDAO.existsByEmail(detail.email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }

        // 3. Check if Phone number already exists
        if (createUserDAO.existsByPhone(detail.phone)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number is already in use");
        }

        createUserDAO.insertUser(user);
        detail.setUserId(user.id);
        createUserDAO.insertUserDetail(detail);
    }
}
