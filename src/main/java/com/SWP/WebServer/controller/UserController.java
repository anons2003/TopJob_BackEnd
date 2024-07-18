package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.*;
import com.SWP.WebServer.entity.RoleType;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.response.LoginResponse;
import com.SWP.WebServer.service.RoleTypeService;
import com.SWP.WebServer.service.UserService;
import com.SWP.WebServer.token.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @Autowired
    private RoleTypeService roleTypeService;

    @GetMapping("/usertypes")
    public List<RoleType> getAllUserTypes() {
        return roleTypeService.getAllUserTypesExcludingAdmin();
    }

    @GetMapping("/users/list")
    public List<User> getAllUser(){
        return userService.getAllUsers();
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


    @PatchMapping("/update-profile")
    public User updateProfile(
            @RequestBody UpdateProfileDTO body,
            @RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);
        return userService.updateProfile(body, userId);
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
