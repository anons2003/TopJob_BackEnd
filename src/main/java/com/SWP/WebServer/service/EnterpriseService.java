//package com.SWP.WebServer.service;
//
//import com.SWP.WebServer.dto.*;
//import com.SWP.WebServer.entity.Enterprise;
//import com.SWP.WebServer.exception.ApiRequestException;
//import com.SWP.WebServer.repository.EnterpriseRepository;
//import com.SWP.WebServer.token.JwtTokenProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.security.SecureRandom;
//
//@Service
//public class EnterpriseService {
//    @Autowired
//    EnterpriseRepository enterpriseRepository;
//    @Autowired
//    EmailService emailService;
//    @Autowired
//    EmailTemplateService emailTemplateService;
//    @Autowired
//    JwtTokenProvider jwtTokenProvider;
//    int strength = 10;
//    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());
//
//    // --Ham gui lai mail verify--//
//    public void reverify(String email) {
//        try {
//            String htmlContent = emailTemplateService.getVerifyMailTemplateEn(
//                    "Tap the button below to confirm your email address",
//                    "Verify", email);
//            emailService.sendMail(email, "Verify email", htmlContent);
//        } catch (Exception e) {
//            throw new ApiRequestException("Failed to send mail", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//
//    // --Ham tim User bang Email--//
//    public Enterprise findEnterpriseByEmail(String email) {
//        return enterpriseRepository.findByEmail(email);
//    }
//
//    // --Ham signup--//
//    public Enterprise signup(SignupEDTO en) {
//        String enterprise_name = en.getEnterprise_name();
//        String email = en.getEmail().toLowerCase();
//        String password = bCryptPasswordEncoder.encode(en.getPassword());
//        if (enterpriseRepository.findByEmailAndSid(email, null) != null) {
//            throw new ApiRequestException("Email already exist", HttpStatus.BAD_REQUEST);
//        }
//        try {
//            String htmlContent = emailTemplateService.getVerifyMailTemplateEn(
//                    "Tap the button below to confirm your email address",
//                    "Verify", email);
//            emailService.sendMail(email, "Verify email", htmlContent);
//        } catch (Exception e) {
//            throw new ApiRequestException("Failed to send mail", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        Enterprise newEnterprise = enterpriseRepository.save(new Enterprise(enterprise_name, email, password,
//                "http://res.cloudinary.com/dswewjrly/image/upload/v1715831315/wmndhsmpxuihewekekzy.jpg", null, 0));
//        newEnterprise.setPassword("");
//        return newEnterprise;
//    }
//
//    // --Ham update bang verify email--//
//    public Enterprise updateVerifyEmail(String token) {
//        String email = "";
//        try {
//            email = jwtTokenProvider.verifyToken(token);
//        } catch (Exception e) {
//            throw new ApiRequestException("Invalid token", HttpStatus.BAD_REQUEST);
//        }
//        Enterprise enterprise = enterpriseRepository.findByEmailAndSid(email, null);
//        enterprise.setIs_verify_email(1);
//        return enterpriseRepository.save(enterprise);
//    }
//
//    // --Ham reset password--//
//    public void resetPassword(ResetPasswordDTO body) {
//        String email = body.getEmail().toLowerCase();
//        String html = emailTemplateService.getResetPasswordMailTemplateForEn("Click here to reset password",
//                "Reset password", email);
//        try {
//            emailService.sendMail(email, "Reset password", html);
//        } catch (Exception e) {
//            throw new ApiRequestException("Can't send email", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    public void changePassword(ChangePasswordDTO body) {
//        String email = "";
//        try {
//            email = jwtTokenProvider.verifyToken(body.getToken());
//        } catch (Exception e) {
//            throw new ApiRequestException("Invalid token!", HttpStatus.BAD_REQUEST);
//        }
//
//        Enterprise enterprise = enterpriseRepository.findByEmailAndSid(email, null);
//        if (enterprise == null) {
//            throw new ApiRequestException("User not found!", HttpStatus.BAD_REQUEST);
//        }
//        enterprise.setPassword(bCryptPasswordEncoder.encode(body.getNewPassword()));
//        enterpriseRepository.save(enterprise);
//
//    }
//
//    public Enterprise saveSocialEnterprise(LoginSocialDTO enterprise) {
//        Enterprise EnExist = enterpriseRepository.findBySid(enterprise.getS_id());
//        if (EnExist != null) {
//            return EnExist;
//        }
//        enterpriseRepository.save(
//                new Enterprise(enterprise.getName(), enterprise.getEmail().toLowerCase(), null, enterprise.getPicture(),
//                        enterprise.getS_id(), 1));
//        Enterprise createdEnterprise = enterpriseRepository.findBySid(enterprise.getS_id());
//        return createdEnterprise;
//    }
//
//    public Enterprise login(LoginDTO body) {
//        String email = body.getEmail().toLowerCase();
//        Enterprise enterprise = enterpriseRepository.findByEmailAndSid(email, null);
//        if (enterprise == null) {
//            throw new ApiRequestException("Email not found", HttpStatus.BAD_REQUEST);
//        }
//        if (enterprise.getUser().getIs_verify_email() == 0) {
//            throw new ApiRequestException("not_verify_yet", HttpStatus.BAD_REQUEST);
//        }
//        String password = body.getPassword();
//        boolean isCorrectPassword = bCryptPasswordEncoder.matches(password, enterprise.getUser().getPassword());
//        if (isCorrectPassword == false) {
//            throw new ApiRequestException("Wrong password", HttpStatus.BAD_REQUEST);
//        }
//        return enterprise;
//    }
//
//    public Enterprise getEnterpriseProfile(String id) {
//        Enterprise enterprise = enterpriseRepository.findByEid(Integer.parseInt(id));
//        return enterprise;
//    }
//
//    public void updatePassword(UpdatePasswordDTO body, String enterpriseId) {
//        String newPassword = body.getNewPassword();
//        String oldPassword = body.getOldPassword();
//        Enterprise enterprise = enterpriseRepository.findByEid(Integer.parseInt(enterpriseId));
//        if (enterprise == null) {
//            throw new ApiRequestException("User not found", HttpStatus.BAD_REQUEST);
//        }
//        if (!bCryptPasswordEncoder.matches(oldPassword, enterprise.getUser().getPassword())) {
//            throw new ApiRequestException("old password wrong", HttpStatus.BAD_REQUEST);
//        }
//        enterprise.getUser().setPassword(bCryptPasswordEncoder.encode(newPassword));
//        enterpriseRepository.save(enterprise);
//    }
//
//    public Enterprise updateProfile(UpdateProfileEnDTO body, String enterpriseId) {
//
//        String Newenterprise_name = body.getPenterprise_name();
//        Enterprise enterprise = enterpriseRepository.findByEid(Integer.parseInt(enterpriseId));
//        enterprise.setEnterprise_name(Newenterprise_name);
//        return enterpriseRepository.save(enterprise);
//    }
//
//    public void updateAvatar(String url, String enterpriseId) {
//        Enterprise enterprise = enterpriseRepository.findByEid(Integer.parseInt(enterpriseId));
//        enterprise.getUser().setAvatar_url(url);
//        enterpriseRepository.save(enterprise);
//    }
//
////    public Enterprise updateInfo(UpdateInfoEnDTO body, String enterpriseId) {
////        String newPhone = body.getPhone();
////        String newLocation = body.getLocation();
////        String newEn_position = body.getEn_position();
////        String newTaxcode = body.getTaxCode();
////        Enterprise enterprise = enterpriseRepository.findByEid(Integer.parseInt(enterpriseId));
////        enterprise.setPhone(newPhone);
////        enterprise.setLocation(newLocation);
////        enterprise.setEn_position(newEn_position);
////        enterprise.setTaxcode(newTaxcode);
////        return enterpriseRepository.save(enterprise);
////    }
//}
