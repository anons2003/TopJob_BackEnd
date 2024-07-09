package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTypeRepository extends JpaRepository<JobType, Long> {
}
