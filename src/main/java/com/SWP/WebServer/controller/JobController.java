package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobPostService jobPostService;


    @GetMapping("/getjobs")
    public ResponseEntity<?> getAllJobs(){
        List<Job> jobs = jobPostService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job savedJob = jobPostService.saveJob(job);
        return ResponseEntity.ok(savedJob);
    }
}

