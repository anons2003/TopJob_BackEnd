package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.AppliedCVDto;
import com.SWP.WebServer.dto.ContactInfoDto;
import com.SWP.WebServer.dto.UpdateInfoDTO;
import com.SWP.WebServer.entity.CVApply;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.repository.JobSeekerRepository;
import com.SWP.WebServer.service.*;
import com.SWP.WebServer.service.Impl.CVService;
import com.SWP.WebServer.service.Impl.JobSeekerService;
import com.SWP.WebServer.token.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/jobSeeker")
public class JobSeekerController {
    @Autowired
    private final JobSeekerRepository jobSeekerRepository;
    private UserService userService;
    private JobSeekerService jobSeekerService;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CVService cvService;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    private EmailService emailService;

    public JobSeekerController(JobSeekerRepository jobSeekerRepository) {
        this.jobSeekerRepository = jobSeekerRepository;
    }

    @GetMapping("/candidate-profile")
    public JobSeeker getProfile(@RequestHeader("Authorization") String token) {
        String userId = null;
        try {
            userId = jwtTokenProvider.verifyToken(token);
        } catch (ExpiredJwtException e) {
            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        JobSeeker jobSeeker = jobSeekerService.getUserProfile(userId);
        return jobSeeker;
    }
    @GetMapping("/candidate-profile/{jid}")
    public JobSeeker getProfileByJid(@PathVariable("jid" ) int userId) {

        JobSeeker jobSeeker = jobSeekerService.getUserProfileByJid(userId);
        return jobSeeker;
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

    @PatchMapping("/update-contact-info")
    public ResponseEntity<?> updateContactInfo(
            @RequestBody ContactInfoDto body,
            @RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);
        jobSeekerService.updateContactInfo(body, userId);
        return ResponseEntity.ok("Update contact successfully");
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


//    @GetMapping("bookmarks")
//    public  ResponseEntity<?> bookmarkJob(@RequestHeader("Authorization") String token){
//        String userId = getUserIdFromToken(token);
//        Job job = jobSeekerService.getBookmarks(userId);
//
//    }

    @PostMapping("/job/{jobId}")
    public ResponseEntity<Job> bookmarkJob(@RequestHeader("Authorization") String token, @PathVariable Long jobId) {
        String userId = getUserIdFromToken(token);
        Job job = bookmarkService.bookmarkJob(Integer.parseInt(userId), jobId);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/job/{jobId}")
    public ResponseEntity<?> unbookmarkJob(@RequestHeader("Authorization") String token, @PathVariable Long jobId) {
        String userId = getUserIdFromToken(token);
        String message = bookmarkService.unbookmarkJob(Integer.parseInt(userId), jobId);
        return ResponseEntity.ok(message);
    }



    @GetMapping("/bookmarks")
    public ResponseEntity<?> getBookmarkedJobs(@RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token);
        List<Job> bookmarkedJobs = bookmarkService.getBookmarkedJobs(userId);
        return ResponseEntity.ok().body(bookmarkedJobs);
    }

    @PostMapping("/apply-cv/{eid}")
    public ResponseEntity<?> applyForJob(@RequestBody AppliedCVDto body,
                                         @RequestHeader("Authorization") String token,
                                         @PathVariable("eid") int eid) {
        String userId = getUserIdFromToken(token);
        CVApply cvApply = cvService.applyCV(body, userId, eid);
        return ResponseEntity.ok(cvApply);
    }

    @PatchMapping("/upload-resume/{eid}")
    public ResponseEntity<?> updateResume(
            @RequestParam(value = "resume", required = false) MultipartFile resume,
            @RequestParam(value = "folder", defaultValue = "user_resume") String folder,
            @RequestHeader("Authorization") String token,
            @PathVariable("eid") int eid) {
        String userId = getUserIdFromToken(token);

        // Lấy tên file gốc không bao gồm phần mở rộng
        String originalFilename = resume.getOriginalFilename();
        String publicId = originalFilename != null ? originalFilename.split("\\.")[0] : "";
        Map<String, Object> data = cloudinaryService.upload(resume, publicId, folder);

        String url = (String) data.get("url");
        cvService.uploadResume(url, userId,eid);
        return ResponseEntity.ok("Update resume successfully");
    }


    @PatchMapping("/reapply-cv/{eid}")
    public ResponseEntity<?> reApplyForJob(@RequestBody AppliedCVDto body,
                                         @RequestHeader("Authorization") String token,
                                         @PathVariable("eid") int eid) {
        String userId = getUserIdFromToken(token);
        CVApply cvApply = cvService.reApplyCV(body, userId, eid);
        return ResponseEntity.ok(cvApply);
    }

    @DeleteMapping("/delete-cv/{eid}")
    public ResponseEntity<?> deleteCV(@RequestHeader("Authorization") String token,
                                           @PathVariable("eid") int eid) {
        String userId = getUserIdFromToken(token);
        String message =cvService.deleteCV(userId, eid);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/get-cvs")
    public ResponseEntity<?> GetAllCVByUserId(@RequestHeader("Authorization") String token
                                      ) {
        String userId = getUserIdFromToken(token);
        List<CVApply> cvApplyList= cvService.GetAllCVByUserId(userId);
        return ResponseEntity.ok(cvApplyList);
    }

    private String getUserIdFromToken(String token) {
        try {
            return jwtTokenProvider.verifyToken(token);
        } catch (ExpiredJwtException e) {
            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send/{jid}")
    public ResponseEntity<?>  sendEmailByJobseeker(@PathVariable("jid") int jid,
                                                   @RequestParam("name") String name,
                                                   @RequestParam("email") String email,
                                                   @RequestParam("subject") String subject,
                                                   @RequestParam("body") String body


    ){
        String message =emailService.sendMailFromJobSeeker(jid,name,email,subject,body);

        return ResponseEntity.ok(message);
    }


}
