package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.OptionDetailSelectServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.OptionDetailSelectServiceResponse;

public interface OptionDetailSelectService {
     OptionDetailSelectServiceResponse changeStatus(OptionDetailSelectServiceRequest request);
}
