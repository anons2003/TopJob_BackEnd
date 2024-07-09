package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.repository.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    // Save a job post
    public Job saveJob(Job job) {
        return jobPostRepository.save(job);
    }

    // Count total job posts
    public long countJobs() {
        return jobPostRepository.count();
    }

    // Get all job posts
    public List<Job> getAllJobs() {
        return jobPostRepository.findAll();
    }

    // Find a job post by ID
    public Optional<Job> getJobById(Long id) {
        return jobPostRepository.findById(id);
    }

    // Update a job post
    public Job updateJob(Long id, Job jobDetails) {
        Optional<Job> jobOptional = jobPostRepository.findById(id);

        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(jobDetails.getTitle());
            job.setDescription(jobDetails.getDescription());
            job.setJobType(jobDetails.getJobType());
            job.setJobCategory(jobDetails.getJobCategory());
            job.setSalaryType(jobDetails.getSalaryType());
            job.setMinSalary(jobDetails.getMinSalary());
            job.setMaxSalary(jobDetails.getMaxSalary());
            job.setSkills(jobDetails.getSkills());
            job.setQualifications(jobDetails.getQualifications());
            job.setExperience(jobDetails.getExperience());
            job.setIndustry(jobDetails.getIndustry());
            job.setAddress(jobDetails.getAddress());
            job.setCountry(jobDetails.getCountry());
            job.setState(jobDetails.getState());
            job.setUpdatedAt(jobDetails.getUpdatedAt());

            return jobPostRepository.save(job);
        } else {
            throw new RuntimeException("Job not found with id " + id);
        }
    }

    // Delete a job post
    public void deleteJob(Long id) {
        jobPostRepository.deleteById(id);
    }
}
