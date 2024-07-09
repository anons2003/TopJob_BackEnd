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
    @Autowired
    private JobPostService jobPostService;

    @PostMapping("/save")
    public Job saveJob(@RequestBody Job job) {
        return jobPostService.saveJob(job);
    }

    @GetMapping("/count")
    public long countJobs() {
        return jobPostService.countJobs();
    }

    @GetMapping("/all")
    public List<Job> getAllJobs() {
        return jobPostService.getAllJobs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Optional<Job> job = jobPostService.getJobById(id);
        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        try {
            Job updatedJob = jobPostService.updateJob(id, jobDetails);
            return ResponseEntity.ok(updatedJob);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobPostService.deleteJob(id);
        return ResponseEntity.ok().build();
    }
}
