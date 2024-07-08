package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobPostService jobPostService;

    @Autowired
    public JobController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
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
        List<Job> jobs = jobPostService.getAllJobs();
        return ResponseEntity.ok().body(jobs);
    }
    @GetMapping("/view/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Optional<Job> job = jobPostService.getJobById(id);
        return job.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
