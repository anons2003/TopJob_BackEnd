package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.*;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.exception.ResourceNotFoundException;
import com.SWP.WebServer.repository.UserRepository;
import com.SWP.WebServer.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    int strength = 10;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());//set mat khau Bcryt trong dtb
    @Autowired
    EmailService emailService;
    @Autowired
    EmailTemplateService emailTemplateService;


    //--Ham gui lai mail verify--//
    public void reverify(String email) {
        try {
            String htmlContent = emailTemplateService.getVerifyMailTemplate("Tap the button below to confirm your email address",
                    "Verify", email);
            emailService.sendMail(email, "Verify email", htmlContent);
        } catch (Exception e) {
            throw new ApiRequestException("Failed to send mail", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //--Ham tim User bang Email--//
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //--Ham signup--//
    public User signup(SignupDTO user) {
        String user_name = user.getUser_name();
        String email = user.getEmail().toLowerCase();
        String password = bCryptPasswordEncoder.encode(user.getPassword());
        if (userRepository.findByEmailAndSid(email, null) != null) {
            throw new ApiRequestException("Email already exist", HttpStatus.BAD_REQUEST);
        }
        try {
            String htmlContent = emailTemplateService.getVerifyMailTemplate("Tap the button below to confirm your email address",
                    "Verify", email);
            emailService.sendMail(email, "Verify email", htmlContent);
        } catch (Exception e) {
            throw new ApiRequestException("Failed to send mail", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User newUser = userRepository.save(new User(user_name, email, password, "https://res.cloudinary.com/dz9kynjwb/image/upload/v1717770585/OIP_bsmlku.jpg", null, 0));
        newUser.setPassword("");
        return newUser;

    }

    //--Ham update bang verify email--//
    public User updateVerifyEmail(String token) {
        String email = "";
        try {
            email = jwtTokenProvider.verifyToken(token);
        } catch (Exception e) {
            throw new ApiRequestException("Invalid token", HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByEmailAndSid(email, null);
        user.setIs_verify_email(1);
        return userRepository.save(user);
    }

    //--Ham reset password--//
    public void resetPassword(ResetPasswordDTO body) {
        String email = body.getEmail().toLowerCase();
        String html = emailTemplateService.getResetPasswordMailTemplate("Click here to reset password", "Reset password", email);
        try {
            emailService.sendMail(email, "Reset password", html);
        } catch (Exception e) {
            throw new ApiRequestException("Can't send email", HttpStatus.BAD_REQUEST);
        }
    }

    public void changePassword(ChangePasswordDTO body) {
        String email = "";
        try {
            email = jwtTokenProvider.verifyToken(body.getToken());
        } catch (Exception e) {
            throw new ApiRequestException("Invalid token!", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmailAndSid(email, null);
        if (user == null) {
            throw new ApiRequestException("User not found!", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(bCryptPasswordEncoder.encode(body.getNewPassword()));
        userRepository.save(user);
    }

    public User saveSocialUser(LoginSocialDTO user) {
        User userExist = userRepository.findBySid(user.getS_id());
        if (userExist != null) {
            return userExist;
        }
        userRepository.save(
                new
                        User(user.getName(), user.getEmail().toLowerCase(), null, user.getPicture(), user.getS_id(), 1));
        User createdUser = userRepository.findBySid(user.getS_id());
        return createdUser;
    }

    public User login(LoginDTO body) {
        String email = body.getEmail().toLowerCase();
        User user = userRepository.findByEmailAndSid(email, null);
        if (user == null) {
            throw new ApiRequestException("Email not found", HttpStatus.BAD_REQUEST);
        }
        if (user.getIs_verify_email() == 0) {
            throw new ApiRequestException("not_verify_yet", HttpStatus.BAD_REQUEST);
        }
        String password = body.getPassword();
        boolean isCorrectPassword = bCryptPasswordEncoder.matches(password, user.getPassword());
        if (isCorrectPassword == false) {
            throw new ApiRequestException("Wrong password", HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    public User getUserProfile(String id) {
        User user = userRepository.findById(Integer.parseInt(id));
        return user;
    }

    public User updateContactInfo(
            ContactInfoDto body,
            String userId) {
        User user = userRepository.findById(Integer.parseInt(userId));
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        // Update contact information
        if (body.getWeb_url() != null) {
            user.setWeb_url(body.getWeb_url());
        }
        if (body.getPhone() != null) {
            user.setPhone(body.getPhone());
        }

        return userRepository.save(user);
    }

    public void updatePassword(
            UpdatePasswordDTO body,
            String userId) {
        String newPassword = body.getNewPassword();
        String oldPassword = body.getOldPassword();
        User user = userRepository.findById(Integer.parseInt(userId));
        if (user == null) {
            throw new ApiRequestException("User not found", HttpStatus.BAD_REQUEST);
        }
        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ApiRequestException("old password wrong", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);

    }


    public User updateProfile(
            UpdateProfileDTO body,
            String userId) {

        String puser_name = body.getPuser_name();
        User user = userRepository.findById(Integer.parseInt(userId));
        user.setUser_name(puser_name);
        return userRepository.save(user);
    }


    public User updateInfo(
            UpdateInfoDTO body,
            String userId) {
        int id = Integer.parseInt(userId);
        User user = userRepository.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        // Update city if not null
        if (body.getCity() != null) {
            user.setCity(body.getCity());
        }

        // Update state if not null
        if (body.getState() != null) {
            user.setState(body.getState());
        }

        // Update first_name if not null
        if (body.getFirst_name() != null) {
            user.setFirst_name(body.getFirst_name());
        }

        // Update last_name if not null
        if (body.getLast_name() != null) {
            user.setLast_name(body.getLast_name());
        }

        // Update user_name if not null
        if (body.getUser_name() != null) {
            user.setUser_name(body.getUser_name());
        }

        // Update occupation if not null
        if (body.getOccupation() != null) {
            user.setOccupation(body.getOccupation());
        }

        // Update intro if not null
        if (body.getIntro() != null) {
            user.setIntro(body.getIntro());
        }

        // Update email if not null
        if (body.getEmail() != null) {
            user.setEmail(body.getEmail());
        }

        // Update resume_url if not null
//        if (body.getResume_url() != null) {
//            user.setResume_url(body.getResume_url());
//        }

        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(Integer.parseInt(userId));
        userRepository.delete(user);
    }

    public void updateAvatar(
            String url,
            String userId) {
        User user = userRepository.findById(Integer.parseInt(userId));
        user.setAvatar_url(url);
        userRepository.save(user);
    }

    public void updateResume(
            String url,
            String userId) {
        User user = userRepository.findById(Integer.parseInt(userId));
        user.setResume_url(url);
        userRepository.save(user);
    }
}
