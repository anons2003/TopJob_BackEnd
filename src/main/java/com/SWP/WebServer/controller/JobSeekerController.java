package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.AppliedCVDto;
import com.SWP.WebServer.entity.CVApply;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.service.BookmarkService;
import com.SWP.WebServer.service.CVServiceImpl;
import com.SWP.WebServer.service.CloudinaryService;
import com.SWP.WebServer.service.Impl.CVService;
import com.SWP.WebServer.service.Impl.JobSeekerService;
import com.SWP.WebServer.service.JobSeekerServiceImpl;
import com.SWP.WebServer.token.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobSeeker")
public class JobSeekerController {
    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CVService cvService;

    @Autowired
    CloudinaryService cloudinaryService;

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
}
