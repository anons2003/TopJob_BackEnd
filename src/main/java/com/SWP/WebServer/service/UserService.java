package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.*;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.entity.RoleType;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.repository.EnterpriseRepository;
import com.SWP.WebServer.repository.JobSeekerRepository;
import com.SWP.WebServer.repository.RoleTypeRepository;
import com.SWP.WebServer.repository.UserRepository;
import com.SWP.WebServer.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JobSeekerRepository jobSeekerRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Autowired
    RoleTypeRepository roleTypeRepository;
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

    public User addNew(User user) {
        user.setCreated_at(new Date(System.currentTimeMillis()));
        int userTypeId = user.getRoleType().getRoleTypeId();

        if (userTypeId == 1) {
            jobSeekerRepository.save(new JobSeeker(user));
        } else {
            enterpriseRepository.save(new Enterprise(user));
        }

        return user;
    }

    //--Ham signup--//
    public User signup(SignupDTO user) {
        String user_name = user.getUser_name();
        String email = user.getEmail().toLowerCase();
        String password = bCryptPasswordEncoder.encode(user.getPassword());
        int roleTypeId = user.getUserTypeId();
        if (userRepository.findByEmail(email) != null) {
            throw new ApiRequestException("Email already exist", HttpStatus.BAD_REQUEST);
        }
        try {
            String htmlContent = emailTemplateService.getVerifyMailTemplate("Tap the button below to confirm your email address",
                    "Verify", email);
            emailService.sendMail(email, "Verify email", htmlContent);
        } catch (Exception e) {
            throw new ApiRequestException("Failed to send mail", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User newUser = userRepository.save(
                new User(
                        user_name, email, password,
                        "http://res.cloudinary.com/dswewjrly/image/upload/v1715831315/wmndhsmpxuihewekekzy.jpg",
                        null, 0
                ));
        RoleType roleType = roleTypeRepository.findByRoleTypeId(roleTypeId)
                .orElseThrow(() -> new ApiRequestException("Role type not found", HttpStatus.BAD_REQUEST));

        newUser.setRoleType(roleType);

        User savedUser = userRepository.save(newUser);
        addNew(savedUser);  // Call the addNew method
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
        User user = userRepository.findByEmailAndGid(email, null);
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

        User user = userRepository.findByEmailAndGid(email, null);
        if (user == null) {
            throw new ApiRequestException("User not found!", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(bCryptPasswordEncoder.encode(body.getNewPassword()));
        userRepository.save(user);
    }

    public User saveSocialUser(LoginSocialDTO user) {
        User userExist = userRepository.findByGid(user.getS_id());
        if (userExist != null) {
            return userExist;
        }
        userRepository.save(
                new
                        User(user.getName(), user.getEmail().toLowerCase(), null, user.getPicture(), user.getS_id(), 1));
        User createdUser = userRepository.findByGid(user.getS_id());
        return createdUser;
    }

    public User login(LoginDTO body) {
        String email = body.getEmail().toLowerCase();
        User user = userRepository.findByEmailAndGid(email, null);
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
        User user = userRepository.findByUid(Integer.parseInt(id));
        return user;
    }

    public User updateContactInfo(
            ContactInfoDto body,
            String userId) {
        User user = userRepository.findByUid(Integer.parseInt(userId));
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
        User user = userRepository.findByUid(Integer.parseInt(userId));
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
        User user = userRepository.findByUid(Integer.parseInt(userId));
        user.setUser_name(puser_name);
        return userRepository.save(user);
    }


    public void deleteUser(String userId) {
        User user = userRepository.findByUid(Integer.parseInt(userId));
        userRepository.delete(user);
    }

    public void updateAvatar(
            String url,
            String userId) {
        User user = userRepository.findByUid(Integer.parseInt(userId));
        user.setAvatar_url(url);
        userRepository.save(user);
    }

    public void updateResume(
            String url,
            String userId) {
        User user = userRepository.findByUid(Integer.parseInt(userId));
        user.setResume_url(url);
        userRepository.save(user);
    }
}
