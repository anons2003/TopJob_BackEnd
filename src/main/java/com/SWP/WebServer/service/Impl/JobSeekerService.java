package com.SWP.WebServer.service.Impl;

import com.SWP.WebServer.dto.ContactInfoDto;
import com.SWP.WebServer.dto.UpdateInfoDTO;
import com.SWP.WebServer.entity.JobSeeker;

public interface JobSeekerService {
    void updateContactInfo(
            ContactInfoDto body,
            String userId);

    JobSeeker updateInfo(
            UpdateInfoDTO updateInfoDTO,
            String userId);

    void updateAvatar(
            String url,
            String userId);

    void updateResume(
            String url,
            String userId);



}
