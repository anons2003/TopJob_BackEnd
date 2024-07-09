package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.JobType;
import com.SWP.WebServer.repository.JobTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobTypeService {

    private final JobTypeRepository jobTypeRepository;

    @Autowired
    public JobTypeService(JobTypeRepository jobTypeRepository) {
        this.jobTypeRepository = jobTypeRepository;
    }

    public List<JobType> getAllJobTypes() {
        return jobTypeRepository.findAll();
    }
}
