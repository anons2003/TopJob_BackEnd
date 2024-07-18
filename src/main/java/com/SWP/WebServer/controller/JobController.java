package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.JobDTO;
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
public class JobController {
    @Autowired
    private JobPostService jobPostService;
    @Autowired
    private JobPostRepository jobRepository;

    // get jobs
    @GetMapping("/getjob")
    public ResponseEntity<?> getAllJobs() {
        List<Job> jobs = jobPostService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }
    //getjob2.0
    @GetMapping("/getjobs")
    public ResponseEntity<List<JobDTO>> getAllJobsDTO() {
        List<JobDTO> jobDTOs = jobPostService.getAllJobDTOs();
        return ResponseEntity.ok().body(jobDTOs);
    }
    //get job by id from url
    @GetMapping("/getjobs/{jobId}")
    public ResponseEntity<?> getJobByJobId(@PathVariable("jobId") Long jobId){
        Optional<Job> job = jobPostService.getJobById(jobId);
        return ResponseEntity.ok(job);
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
    //??
    @GetMapping("/view/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Optional<Job> job = jobPostService.getJobById(id);
        return job.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //approve job
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
    //active job
    @PatchMapping("/toggle-active/{id}")
    public ResponseEntity<?> toggleActive(@PathVariable Long id) {
        try {
            jobPostService.toggleActiveStatus(id);
            return ResponseEntity.ok("Job active status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
