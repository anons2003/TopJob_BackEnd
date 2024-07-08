package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.ToggleActiveDTO;
import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.service.JobSeekerService;
import com.SWP.WebServer.repository.JobSeekerRepository;
import com.SWP.WebServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/job-seekers")
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;
    private final JobSeekerRepository jobSeekerRepository;
    private UserService userService;

    @Autowired
    public JobSeekerController(JobSeekerService jobSeekerService, JobSeekerRepository jobSeekerRepository) {
        this.jobSeekerService = jobSeekerService;
        this.jobSeekerRepository = jobSeekerRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<List<JobSeeker>> getAllJobSeekers() {
        List<JobSeeker> jobSeekers = jobSeekerService.getAllJobSeekers();
        return ResponseEntity.ok(jobSeekers);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<JobSeeker> getJobSeekerById(@PathVariable int id) {
        Optional<JobSeeker> jobSeeker = jobSeekerRepository.findById(id);
        if (jobSeeker.isPresent()) {
            return ResponseEntity.ok(jobSeeker.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
