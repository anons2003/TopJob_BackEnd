package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.JobPostDTO;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.service.EnterpriseService;
import com.SWP.WebServer.service.JobPostService;
import com.SWP.WebServer.token.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enterprises")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JobPostService jobPostService;

    @Autowired
    public EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @GetMapping("enterprise-profile")
    public Enterprise getProfile(@RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);
        Enterprise enterprise = enterpriseService.getUserProfile(userId);
        return enterprise;
    }

    // Lưu một bài đăng công việc
    @PostMapping("/save")
    public ResponseEntity<Job> saveJob(@RequestHeader("Authorization") String token,
            @RequestBody JobPostDTO job) {
        String userId = getUserIdFromToken(token);
        Job savedJob = jobPostService.saveJob(userId,job);
        return ResponseEntity.ok().body(savedJob);
    }

    @GetMapping("/get-jobs-post")
    public ResponseEntity<?> getJobByEid(@RequestHeader("Authorization") String token){
        String userId = getUserIdFromToken(token);
        List<Job> jobs = jobPostService.findJobsByUid(userId);

        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/enterprise-profile/{eid}")
    public Enterprise getProfileByJid(@PathVariable("eid") int userId) {

        Enterprise enterprise = enterpriseService.getProfileByEid(userId);
        return enterprise;
    }

    @GetMapping("/enterprise")
    public List<Enterprise> getAllJEnterprise() {
        return enterpriseService.getAllEnterprises();
    }


    @GetMapping("/list")
    public ResponseEntity<List<Enterprise>> getAllEnterprises() {
        List<Enterprise> enterprises = enterpriseService.getAllEnterprises();
        return ResponseEntity.ok(enterprises);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Enterprise> getEnterpriseById(@PathVariable int id) {
        Optional<Enterprise> enterprise = enterpriseService.getEnterpriseById(id);
        return enterprise.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PatchMapping("/toggle-active/{id}")
    public ResponseEntity<?> toggleActive(@PathVariable int id) {
        try {
            enterpriseService.toggleActiveStatus(id);
            return ResponseEntity.ok("Enterprise active status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    private String getUserIdFromToken(String token) {
        try {
            return jwtTokenProvider.verifyToken(token);
        } catch (ExpiredJwtException e) {
            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}