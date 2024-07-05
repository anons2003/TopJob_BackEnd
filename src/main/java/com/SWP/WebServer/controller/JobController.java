package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobPostService jobPostService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job savedJob = jobPostService.saveJob(job);
        return ResponseEntity.ok(savedJob);
    }
    // admin

    @GetMapping("/totalJobs")
    public ResponseEntity<Long> getTotalJobs() {
        long totalJobs = jobPostService.getTotalJobs();
        return ResponseEntity.ok(totalJobs);
    }
}

