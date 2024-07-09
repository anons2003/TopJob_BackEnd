package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.JobCategory;
import com.SWP.WebServer.repository.JobCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobCategoryService {

    private final JobCategoryRepository jobCategoryRepository;

    @Autowired
    public JobCategoryService(JobCategoryRepository jobCategoryRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<JobCategory> getAllJobCategories() {
        return jobCategoryRepository.findAll();
    }
}
