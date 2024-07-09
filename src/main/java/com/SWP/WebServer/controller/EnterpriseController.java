package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.service.EnterpriseService;
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
    public EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @GetMapping("enterprise-profile")
    public Enterprise getProfile(@RequestHeader("Authorization") String token) {
        String userId = null;
        try {
            userId = jwtTokenProvider.verifyToken(token);
        } catch (ExpiredJwtException e) {
            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Enterprise enterprise = enterpriseService.getUserProfile(userId);
        return enterprise;
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
}