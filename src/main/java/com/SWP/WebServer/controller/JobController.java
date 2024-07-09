package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.PackageServiceDTO;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.repository.JobRepository;
import com.SWP.WebServer.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobPostService jobPostService;
    private final JobRepository jobRepository;
    private final JobPostService jobService;

    @Autowired
    public JobController(JobPostService jobPostService, JobRepository jobRepository, JobPostService jobService) {
        this.jobPostService = jobPostService;
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    // get jobs
    @GetMapping("/getjobs")
    public ResponseEntity<?> getAllJobs() {
        List<Job> jobs = jobPostService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    // Lưu một bài đăng công việc
    @PostMapping("/save")
    public ResponseEntity<Job> saveJob(@RequestBody Job job) {
        Job savedJob = jobPostService.saveJob(job);
        return ResponseEntity.ok().body(savedJob);
    }

    // API để đếm tổng số bài đăng công việc
    @GetMapping("/totalJob")
    public ResponseEntity<Long> countJobs() {
        long totalJobs = jobPostService.countJobs();
        return ResponseEntity.ok().body(totalJobs);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Job>> getAllJobsAdmin() {
        List<Job> jobs = jobPostService.getAllJobs().stream()
                .filter(Job::isActive) // Only include active jobs
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(jobs);
    }

    @GetMapping("/inactive-list")
    public ResponseEntity<List<Job>> getAllInactiveJobsAdmin() {
        List<Job> jobs = jobPostService.getAllJobs().stream()
                .filter(job -> !job.isActive()) // Only include inactive jobs
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(jobs);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Optional<Job> job = jobPostService.getJobById(id);
        return job.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/toggle-active/{id}")
    public ResponseEntity<?> toggleActive(@PathVariable Long id) {
        try {
            jobService.toggleActiveStatus(id);
            return ResponseEntity.ok("Job active status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PatchMapping("/approval/{id}")
    public ResponseEntity<?> approveJob(@PathVariable Long id, @RequestBody Map<String, Boolean> approval) {
        boolean isActive = approval.get("isActive");
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setActive(isActive);
            jobRepository.save(job);
            return ResponseEntity.ok("Job status updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Job not found.");
        }
    }
}
