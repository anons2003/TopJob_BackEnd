package com.SWP.WebServer.service.Impl;

import com.SWP.WebServer.dto.ContactInfoDto;
import com.SWP.WebServer.dto.UpdateInfoEnDTO;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.Job;

import java.util.List;
import java.util.Optional;

public interface EnterpriseService {
    void updateContactInfo(
            ContactInfoDto body,
            String userId);

    Enterprise updateInfoEn(
            UpdateInfoEnDTO updateInfoDTO,
            String userId);

    void updateAvatar(
            String url,
            String userId);



    Enterprise getUserProfile(String userId);

    Enterprise getUserProfileByEid(int userId);

    List<Enterprise> getAllEnterprises();

    Optional<Enterprise> getEnterpriseById(int id);

    void toggleActiveStatus(int id);
}
