package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker,Integer> {
    JobSeeker findByJid(int jid);
    JobSeeker findByUser_Uid(int user_id);

    @Query("SELECT b.jobId FROM Bookmark b WHERE b.jobSeekers.jid = :user_id")
    List<Job> findAllBookmarks(@Param("user_id") int user_id);
}
