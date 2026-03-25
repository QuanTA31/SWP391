package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.Location;

import java.util.List;

public interface LocationService {
    List<Location> selectLocationsWithAssets();
}
