package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker,Integer> {
    JobSeeker findByJid(int jid);
}
