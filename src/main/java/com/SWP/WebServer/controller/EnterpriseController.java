//package com.SWP.WebServer.controller;
//
//
//import com.SWP.WebServer.dto.*;
//import com.SWP.WebServer.entity.Enterprise;
//import com.SWP.WebServer.exception.ApiRequestException;
//import com.SWP.WebServer.response.LoginResponse;
//
//import com.SWP.WebServer.service.CloudinaryService;
////import com.SWP.WebServer.service.EnterpriseService;
//import com.SWP.WebServer.token.JwtTokenProvider;
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/enterprise")
//public class EnterpriseController {
////    @Autowired
////    EnterpriseService enterpriseService;
//    @Autowired
//    JwtTokenProvider jwtTokenProvider;
//    @Autowired
//    CloudinaryService cloudinaryService;
//
//    @PostMapping("/signup")
//    public Enterprise create(@RequestBody SignupEDTO body) {
//        return enterpriseService.signup(body);
//    }
//
////    @PostMapping("/social")
////    public LoginResponse loginSocial(@RequestBody LoginSocialDTO body) {
////        Enterprise enterprise = enterpriseService.saveSocialEnterprise(body);
////        String token = jwtTokenProvider.generateAccessToken(enterprise.getEid() + "");
////        LoginResponse response = new LoginResponse(token, enterprise.getRole());
////        return response;
////    }
//
//    @GetMapping("/verify")
//    public void verifyEmail(@RequestParam(name = "token") String query, HttpServletResponse response) {
//        enterpriseService.updateVerifyEmail(query);
//        try {
//            response.sendRedirect("http://localhost:3000/enterprise/login");
//        } catch (Exception e) {
//            throw new ApiRequestException("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/reverify")
//    public ResponseEntity<?> reverify(@RequestBody ReverifyDTO body) {
//        enterpriseService.reverify(body.getEmail());
//        return ResponseEntity.ok("Reverification email sent successfully.");
//    }
//
////    @PostMapping("/login")
////    public LoginResponse login(@RequestBody LoginDTO body) {
////        Enterprise enterprise = enterpriseService.login(body);
////        String token = jwtTokenProvider.generateAccessToken(enterprise.getEid() + "");
////        LoginResponse response = new LoginResponse(token, enterprise.getRole());
////        return response;
////    }
//
//    @PostMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO body) {
//        enterpriseService.resetPassword(body);
//        return ResponseEntity.ok("Email sent successfully.");
//
//    }
//
//    @PatchMapping("/change-password")
//    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO body) {
//        enterpriseService.changePassword(body);
//        return ResponseEntity.ok("Reset password successfully.");
//    }
//
//    @GetMapping("/profile")
//    public Enterprise getProfile(@RequestHeader("Authorization") String token) {
//        String enterpriseId = null;
//        try {
//            enterpriseId = jwtTokenProvider.verifyToken(token);
//        } catch (ExpiredJwtException e) {
//            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        Enterprise enterprise = enterpriseService.getEnterpriseProfile(enterpriseId);
//        return enterprise;
//    }
//
//    @PatchMapping("/update-password")
//    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDTO body,
//                                            @RequestHeader("Authorization") String token) {
//        String enterpriseId = null;
//        try {
//            enterpriseId = jwtTokenProvider.verifyToken(token);
//        } catch (ExpiredJwtException e) {
//            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        enterpriseService.updatePassword(body, enterpriseId);
//        return ResponseEntity.ok("Update password successfully");
//    }
//
//    @PatchMapping("/update-profile")
//    public Enterprise update(@RequestBody UpdateProfileEnDTO body, @RequestHeader("Authorization") String token) {
//        String enterpriseId = null;
//        try {
//            enterpriseId = jwtTokenProvider.verifyToken(token);
//        } catch (ExpiredJwtException e) {
//            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return enterpriseService.updateProfile(body, enterpriseId);
//    }
//
//    @PatchMapping("/update-avatar")
//    public ResponseEntity<?> update(@RequestParam("image") MultipartFile file,
//                                    @RequestHeader("Authorization") String token) {
//        String enterpriseId = null;
//        try {
//            enterpriseId = jwtTokenProvider.verifyToken(token);
//        } catch (ExpiredJwtException e) {
//            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        Map data = this.cloudinaryService.upload(file);
//        String url = (String) data.get("url");
//        enterpriseService.updateAvatar(url, enterpriseId);
//        return new ResponseEntity<>("update avatar successfully", HttpStatus.OK);
//    }
//
//    @PatchMapping("/update-about")
//    public Enterprise update(@RequestBody UpdateInfoEnDTO body, @RequestHeader("Authorization") String token) {
//        String enterpriseId = null;
//        try {
//            enterpriseId = jwtTokenProvider.verifyToken(token);
//        } catch (ExpiredJwtException e) {
//            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return enterpriseService.updateInfo(body, enterpriseId);
//    }
//}
