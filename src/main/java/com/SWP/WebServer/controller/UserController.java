package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.*;
import com.SWP.WebServer.entity.CVApply;
import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.entity.RoleType;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.repository.RoleTypeRepository;
import com.SWP.WebServer.repository.UserRepository;
import com.SWP.WebServer.response.LoginResponse;
import com.SWP.WebServer.service.CloudinaryService;
import com.SWP.WebServer.service.Impl.CVService;
import com.SWP.WebServer.service.Impl.JobSeekerService;
import com.SWP.WebServer.service.JobSeekerServiceImpl;
import com.SWP.WebServer.service.UserService;
import com.SWP.WebServer.token.JwtTokenProvider;
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
import java.util.Optional;

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
    CVService cvService;
    @Autowired
    RoleTypeRepository roleTypeRepository;
    //admin
    @Autowired
    private UserRepository userRepository;

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

    @PostMapping("/apply-cv/{eid}")
    public ResponseEntity<?> applyForJob(@RequestBody AppliedCVDto body,
                                         @RequestHeader("Authorization") String token,
                                         @PathVariable("eid") int eid) {
        String userId = getUserIdFromToken(token);
        CVApply cvApply = cvService.applyCV(body, userId, eid);
        return ResponseEntity.ok(cvApply);
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

//    @PatchMapping("/update-resume")
//    public ResponseEntity<?> updateResume(
//            @RequestParam(value = "resume", required = false) MultipartFile resume,
//            @RequestHeader("Authorization") String token) {
//        String userId = getUserIdFromToken(token);
//        Map<String, String> data = cloudinaryService.upload(resume);
//        String url = data.get("url");
//        jobSeekerService.updateResume(url, userId);
//        return ResponseEntity.ok("Update resume successfully");
//    }

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

    // admin
    @GetMapping("/totalUsers")
    public ResponseEntity<Long> getTotalUsers() {
        long totalUsers = userService.getTotalUsers();
        return ResponseEntity.ok(totalUsers);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUserStatus(@PathVariable int id, @RequestBody User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsActive(updatedUser.getIsActive());
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }


}
