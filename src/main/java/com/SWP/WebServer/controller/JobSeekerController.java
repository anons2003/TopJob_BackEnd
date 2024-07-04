package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.service.BookmarkService;
import com.SWP.WebServer.service.Impl.JobSeekerService;
import com.SWP.WebServer.service.JobSeekerServiceImpl;
import com.SWP.WebServer.token.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobSeeker")
public class JobSeekerController {
    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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


    private String getUserIdFromToken(String token) {
        try {
            return jwtTokenProvider.verifyToken(token);
        } catch (ExpiredJwtException e) {
            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
