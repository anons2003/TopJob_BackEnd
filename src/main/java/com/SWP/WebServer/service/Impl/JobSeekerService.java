package com.SWP.WebServer.service.Impl;

import com.SWP.WebServer.dto.ContactInfoDto;
import com.SWP.WebServer.dto.UpdateInfoDTO;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.entity.JobSeeker;

import java.util.List;
import java.util.Optional;

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


    Job  getBookmarks(String userId);

    JobSeeker getUserProfile(String userId);

    JobSeeker getUserProfileByJid(int userId);

    List<JobSeeker> getAllJobSeekers();
    Optional<JobSeeker> getJobSeekerById(int id);
}