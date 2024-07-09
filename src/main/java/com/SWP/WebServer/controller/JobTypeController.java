package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.JobType;
import com.SWP.WebServer.service.JobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/job-types")
public class JobTypeController {

    private final JobTypeService jobTypeService;

    @Autowired
    public JobTypeController(JobTypeService jobTypeService) {
        this.jobTypeService = jobTypeService;
    }

    @GetMapping
    public ResponseEntity<List<JobType>> getAllJobTypes() {
        List<JobType> jobTypes = jobTypeService.getAllJobTypes();
        return ResponseEntity.ok(jobTypes);
    }
}
