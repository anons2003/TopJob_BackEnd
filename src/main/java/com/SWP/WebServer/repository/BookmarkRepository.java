package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.Bookmark;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    Optional<Bookmark> findByJobIdAndJobSeekers(Job jobId, JobSeeker jobSeekers);
}
