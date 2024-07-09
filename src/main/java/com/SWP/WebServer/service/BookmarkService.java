package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.Bookmark;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.repository.BookmarkRepository;
import com.SWP.WebServer.repository.JobPostRepository;
import com.SWP.WebServer.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private JobPostRepository jobRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    public List<Job> getBookmarkedJobs(String userId) {
        return jobSeekerRepository.findAllBookmarks(Integer.parseInt(userId));
    }


    public Job bookmarkJob(int jobSeekerId, Long jobId) {
        JobSeeker jobSeeker = jobSeekerRepository.findByUser_Uid(jobSeekerId);
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        Optional<Bookmark> existingBookmark = bookmarkRepository.findByJobIdAndJobSeekers(job, jobSeeker);

        if (existingBookmark.isPresent()) {
            throw new ApiRequestException("Job Already Bookmarked!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setJobId(job);
        bookmark.setJobSeekers(jobSeeker);
        bookmark.setIsBookmarked((byte) 1);  // 1 for bookmarked

        bookmarkRepository.save(bookmark);

        return job;
    }

    public String unbookmarkJob(int jobSeekerId, Long jobId) {
        JobSeeker jobSeeker = jobSeekerRepository.findByUser_Uid(jobSeekerId);
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        Optional<Bookmark> existingBookmark = bookmarkRepository.findByJobIdAndJobSeekers(job, jobSeeker);

        if (!existingBookmark.isPresent()) {
            throw new ApiRequestException("Job Not Bookmarked!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        bookmarkRepository.delete(existingBookmark.get());

        return "Unbookmark successful";
    }

}
