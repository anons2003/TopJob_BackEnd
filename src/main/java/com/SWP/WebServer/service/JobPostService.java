// JobService.java
package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.repository.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    public List<Job> getAllJobs(){
        return jobPostRepository.findAll();
    }

    public Job saveJob(Job job) {
        return jobPostRepository.save(job);
    }
}