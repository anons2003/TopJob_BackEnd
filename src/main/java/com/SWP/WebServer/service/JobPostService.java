// JobService.java
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


    // Phương thức lưu một bài đăng công việc
    public Job saveJob(Job job) {
        return jobPostRepository.save(job);
    }

    // Phương thức đếm tổng số bài đăng công việc
    public long countJobs() {
        return jobPostRepository.count();
    }
    // Lấy danh sách công việc
    public List<Job> getAllJobs() {
        return jobPostRepository.findAll();
    }
    // Tìm công việc bằng ID
    public Optional<Job> getJobById(Long id) {
        // Triển khai logic tìm công việc theo ID từ cơ sở dữ liệu
        return jobPostRepository.findById(id);
    }
    public void toggleActiveStatus(Long id) {
        Optional<Job> optionalJob = jobPostRepository.findById(id);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            job.setActive(!job.isActive());
            jobPostRepository.save(job);
        } else {
            throw new IllegalArgumentException("Job not found with ID: " + id);
        }
    }
}