package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.repository.JobPostRepository;
import com.SWP.WebServer.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobs")
public class JobPostController {

    private final JobPostService jobPostService;
    private final JobPostRepository jobPostRepository;

    @Autowired
    public JobPostController(JobPostService jobPostService, JobPostRepository jobPostRepository ) {
        this.jobPostService = jobPostService;
        this.jobPostRepository = jobPostRepository;
    }

    // get jobs
    @GetMapping("/getjobs")
    public ResponseEntity<?> getAllJobs() {
        List<Job> jobs = jobPostService.getAllJobs();
        return ResponseEntity.ok(jobs);
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
            jobPostService.toggleActiveStatus(id);
            return ResponseEntity.ok("Job active status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PatchMapping("/approval/{id}")
    public ResponseEntity<?> approveJob(@PathVariable Long id, @RequestBody Map<String, Boolean> approval) {
        boolean isActive = approval.get("isActive");
        Optional<Job> jobOptional = jobPostRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setActive(isActive);
            jobPostRepository.save(job);
            return ResponseEntity.ok("Job status updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Job not found.");
        }
    }
}
