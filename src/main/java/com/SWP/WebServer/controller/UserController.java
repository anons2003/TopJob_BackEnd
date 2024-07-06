package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.*;
import com.SWP.WebServer.entity.CVApply;
import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.entity.RoleType;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.repository.RoleTypeRepository;
import com.SWP.WebServer.response.LoginResponse;
import com.SWP.WebServer.service.CloudinaryService;
import com.SWP.WebServer.service.Impl.CVService;
import com.SWP.WebServer.service.Impl.JobSeekerService;
import com.SWP.WebServer.service.JobSeekerServiceImpl;
import com.SWP.WebServer.service.UserService;
import com.SWP.WebServer.token.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController

public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JobSeekerService jobSeekerService;
    @Autowired
    JobSeekerServiceImpl jobSeekerServiceimpl;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    RoleTypeRepository roleTypeRepository;

    @GetMapping("/usertypes")
    public List<RoleType> getAllUserTypes() {
        return roleTypeRepository.findAll();
    }

    @GetMapping("/candidate-profile")
    public JobSeeker getProfile(@RequestHeader("Authorization") String token) {
        String userId = null;
        try {
            userId = jwtTokenProvider.verifyToken(token);
        } catch (ExpiredJwtException e) {
            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        JobSeeker jobSeeker = jobSeekerServiceimpl.getUserProfile(userId);
        return jobSeeker;
    }


    @PostMapping("/signup")
    public User create(@RequestBody SignupDTO body) {
        return userService.signup(body);
    }

//    @PostMapping("/social")
//    public LoginResponse loginSocial(@RequestBody LoginSocialDTO body) {
//        User user = userService.saveSocialUser(body);
//        String token = jwtTokenProvider.generateAccessToken(user.getId() + "");
//        LoginResponse response = new LoginResponse(token, user.getRole());
//        return response;
//    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO body) {
        userService.resetPassword(body);
        return ResponseEntity.ok("Email sent successfully.");

    }

    @GetMapping("/verify")
    public void verifyEmail(
            @RequestParam(name = "token") String query,
            HttpServletResponse response) {
        userService.updateVerifyEmail(query);
        try {
            response.sendRedirect("http://localhost:3000/login");
        } catch (Exception e) {
            throw new ApiRequestException("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reverify")
    public ResponseEntity<?> reverify(@RequestBody ReverifyDTO body) {
        userService.reverify(body.getEmail());
        return ResponseEntity.ok("Reverification email sent successfully.");

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginDTO body) {
        User user = userService.login(body);
        String token = jwtTokenProvider.generateAccessToken(user.getUid() + "");
        LoginResponse response = new LoginResponse(token, user.getRoleType().getRoleTypeName());
        return response;
    }


    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO body) {
        userService.changePassword(body);
        return ResponseEntity.ok("Reset password successfully.");

    }


    @PatchMapping("/update-password")
    public ResponseEntity<?> updatePassword(
            @RequestBody UpdatePasswordDTO body,
            @RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);
        userService.updatePassword(body, userId);
        return ResponseEntity.ok("Update password successfully");
    }

    @PatchMapping("/update-contact-info")
    public ResponseEntity<?> updateContactInfo(
            @RequestBody ContactInfoDto body,
            @RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);
        jobSeekerService.updateContactInfo(body, userId);
        return ResponseEntity.ok("Update contact successfully");
    }

    @PatchMapping("/update-profile")
    public User updateProfile(
            @RequestBody UpdateProfileDTO body,
            @RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);
        return userService.updateProfile(body, userId);
    }

    @PatchMapping("/update-info")
    public ResponseEntity<?> updateUserInfo(
            @RequestBody UpdateInfoDTO updateInfoDTO,
            @RequestParam(value = "resume", required = false) MultipartFile resume,
            @RequestHeader("Authorization") String token) throws IOException {
        String userId = getUserIdFromToken(token);


        JobSeeker updatedUser = jobSeekerService.updateInfo(updateInfoDTO, userId);
        return ResponseEntity.ok(updatedUser);
    }
//@PatchMapping("/update-info")
//public ResponseEntity<?> updateUserInfo(
//        @RequestPart("updateInfoDTO") String updateInfoDTOJson,
//        @RequestPart(value = "resume", required = false) MultipartFile resume,
//        @RequestParam(value = "folder", defaultValue = "user_resume") String folder,
//        @RequestHeader("Authorization") String token) throws IOException {
//    String userId = getUserIdFromToken(token);
//
//    // Convert JSON string to UpdateInfoDTO object
//    ObjectMapper objectMapper = new ObjectMapper();
//    UpdateInfoDTO updateInfoDTO = objectMapper.readValue(updateInfoDTOJson, UpdateInfoDTO.class);
//
//    if (resume != null && !resume.isEmpty()) {
//        String originalFilename = resume.getOriginalFilename();
//        String publicId = originalFilename != null ? originalFilename.split("\\.")[0] : "";
//        Map<String, Object> data = cloudinaryService.upload(resume, publicId, folder);
//        String url = (String) data.get("url");
//        updateInfoDTO.setResume_url(url);
//    }
//
//    JobSeeker updatedUser = jobSeekerService.updateInfo(updateInfoDTO, userId);
//    return ResponseEntity.ok(updatedUser);
//}

    @PatchMapping("/update-avatar")
    public ResponseEntity<?> updateAvatar(
            @RequestParam("image") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "user_avatars") String folder,
            @RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);

        // Lấy tên file gốc không bao gồm phần mở rộng
        String originalFilename = file.getOriginalFilename();
        String publicId = originalFilename != null ? originalFilename.split("\\.")[0] : "";
        Map<String, Object> data = cloudinaryService.upload(file, publicId, folder);
        String url = (String) data.get("url");
        jobSeekerService.updateAvatar(url, userId);
        return ResponseEntity.ok("Update avatar successfully");
    }

    @PatchMapping("/update-resume")
    public ResponseEntity<?> updateResume(
            @RequestParam(value = "resume", required = false) MultipartFile resume,
            @RequestParam(value = "folder", defaultValue = "user_resume") String folder,
            @RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);

        // Lấy tên file gốc không bao gồm phần mở rộng
        String originalFilename = resume.getOriginalFilename();
        String publicId = originalFilename != null ? originalFilename.split("\\.")[0] : "";
        Map<String, Object> data = cloudinaryService.upload(resume, publicId, folder);
        String url = (String) data.get("url");
        jobSeekerService.updateResume(url, userId);
        return ResponseEntity.ok("Update resume successfully");
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);
        userService.deleteUser(userId);
        return ResponseEntity.ok("Delete User successfully");
    }

    private String getUserIdFromToken(String token) {
        try {
            return jwtTokenProvider.verifyToken(token);
        } catch (ExpiredJwtException e) {
            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/totalUsers")
    public ResponseEntity<Long> countUsers() {
        long totalUsers = userService.countUsers();
        return ResponseEntity.ok().body(totalUsers);
    }
}
