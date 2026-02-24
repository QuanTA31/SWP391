package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AllUserDAO;
import com.example.swp391_assetmanagement.service.ViewAllUserService;
import com.example.swp391_assetmanagement.service.servicerequest.ViewAllUserServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAllUserServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ViewAllUserServiceImpl implements ViewAllUserService {

    private final AllUserDAO allUserDAO;

    @Override
    public List<ViewAllUserServiceResponse> selectAllUser(ViewAllUserServiceRequest request) {
        List<ViewAllUserServiceResponse> userResponses = allUserDAO.selectAllUser(request);

        if (userResponses.isEmpty()) {
            return Collections.emptyList();
        }

        return userResponses;
    }
}
