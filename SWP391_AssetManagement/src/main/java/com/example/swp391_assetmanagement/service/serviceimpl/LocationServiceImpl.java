package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.InventoryDAO;
import com.example.swp391_assetmanagement.entity.Location;
import com.example.swp391_assetmanagement.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final InventoryDAO inventoryDAO;

    @Override
    public List<Location> selectLocationsWithAssets() {
        return inventoryDAO.selectLocationsWithAssets();
    }
}
