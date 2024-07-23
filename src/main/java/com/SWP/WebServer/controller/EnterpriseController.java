package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.ContactInfoDto;
import com.SWP.WebServer.dto.UpdateInfoEnDTO;
import com.SWP.WebServer.entity.CVApply;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.service.CloudinaryService;
import com.SWP.WebServer.service.EmailService;
import com.SWP.WebServer.service.Impl.CVService;
import com.SWP.WebServer.service.Impl.EnterpriseService;
import com.SWP.WebServer.service.JobPostService;
import com.SWP.WebServer.token.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/enterprises")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    private JobPostService jobPostService;
    @Autowired
    private CVService cvService;
    @Autowired
    EmailService emailService;

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
        Enterprise enterprise = enterpriseService.getUserProfileByEid(userId);
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

    @PatchMapping("/update-avatar")
    public ResponseEntity<?> updateAvatar(
            @RequestParam("image") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "company_avatars") String folder,
            @RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);

        // Lấy tên file gốc không bao gồm phần mở rộng
        String originalFilename = file.getOriginalFilename();
        String publicId = originalFilename != null ? originalFilename.split("\\.")[0] : "";
        Map<String, Object> data = cloudinaryService.upload(file, publicId, folder);
        String url = (String) data.get("url");
        enterpriseService.updateAvatar(url, userId);
        return ResponseEntity.ok("Update avatar successfully");
    }


    private String getUserIdFromToken(String token) {
        try {
            return jwtTokenProvider.verifyToken(token);
        } catch (ExpiredJwtException e) {
            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-cv-applied")
    public ResponseEntity<?> getCvByUid(@RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);
        List<CVApply> cvApplies = cvService.findCVByUid(userId);

        return ResponseEntity.ok(cvApplies);
    }

    @PatchMapping("reject-cv/{uid}")// uid of jobSeeker
    public ResponseEntity<?> rejectCV(@RequestHeader("Authorization") String token,
                                      @PathVariable("uid") int uid,
                                      @RequestBody RejectionRequest rejectReason) {
        String enId = getUserIdFromToken(token);
        String message = cvService.rejectCV(enId, uid);
        emailService.sendRejectionEmail(enId, uid, rejectReason.getReasons());

        return ResponseEntity.ok(message);
    }

    //Define to RejectionRequest class to accept json from the front end
    @Data
    public static class RejectionRequest {
        private String[] reasons;
    }

    @PatchMapping("accept-cv/{uid}")// uid of jobSeeker
    public ResponseEntity<?> acceptCV(@RequestHeader("Authorization") String token,
                                      @PathVariable("uid") int uid) {
        String enId = getUserIdFromToken(token);
        String message = cvService.acceptCV(enId, uid);
        emailService.sendAcceptanceEmail(enId, uid);


        return ResponseEntity.ok(message);
    }

    @PatchMapping("/update-contact-info")
    public ResponseEntity<?> updateContactInfo(
            @RequestBody ContactInfoDto body,
            @RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);
        enterpriseService.updateContactInfo(body, userId);
        return ResponseEntity.ok("Update contact successfully");
    }

    @PatchMapping("/update-info")
    public ResponseEntity<?> updateUserInfo(
            @RequestBody UpdateInfoEnDTO updateInfoEnDTO,
            @RequestHeader("Authorization") String token) throws IOException {
        String userId = getUserIdFromToken(token);
        Enterprise updatedUser = enterpriseService.updateInfoEn(updateInfoEnDTO, userId);
        return ResponseEntity.ok(updatedUser);
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

    //get job theo eid rieng biet
    @GetMapping("/jobs/{eid}")
    public ResponseEntity<List<Job>> getJobsByEnterpriseId(@PathVariable int eid) {
        List<Job> jobs = jobPostService.getJobsByEnterpriseId(eid);
        return ResponseEntity.ok(jobs);
    }

    //revert to base condition
    @PatchMapping("revert-cv/{uid}")// uid of jobSeeker
    public ResponseEntity<?> revertCV(@RequestHeader("Authorization") String token,
                                      @PathVariable("uid") int uid) {
        String enId = getUserIdFromToken(token);
        String message = cvService.revertCV(enId, uid);

        return ResponseEntity.ok(message);
    }

    @PostMapping("/send/{eid}")
    public ResponseEntity<?> sendEmailToEnterprise(@PathVariable("eid") int eid,
                                                   @RequestParam("name") String name,
                                                   @RequestParam("email") String email,
                                                   @RequestParam("subject") String subject,
                                                   @RequestParam("body") String body


    ) {
        String message = emailService.sendEmailToEnterprise(eid, name, email, subject, body);

        return ResponseEntity.ok(message);
    }


//    // Lưu một bài đăng công việc
//    @PostMapping("/save")
//    public ResponseEntity<Job> saveJob(@RequestHeader("Authorization") String token,
//                                       @RequestBody JobDTO job) {
//        String userId = getUserIdFromToken(token);
//        Job savedJob = jobPostService.saveJob(userId,job);
//        return ResponseEntity.ok().body(savedJob);
//    }

}