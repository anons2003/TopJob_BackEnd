package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.JobCategory;
import com.SWP.WebServer.service.JobCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/job-categories")
public class JobCategoryController {

    private final JobCategoryService jobCategoryService;

    @Autowired
    public JobCategoryController(JobCategoryService jobCategoryService) {
        this.jobCategoryService = jobCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<JobCategory>> getAllJobCategories() {
        List<JobCategory> jobCategories = jobCategoryService.getAllJobCategories();
        return ResponseEntity.ok(jobCategories);
    }
}
