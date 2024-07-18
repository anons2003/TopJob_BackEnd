package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  JobTypeRepository extends JpaRepository<JobType, Integer> {
    Optional<JobType> findByJobTypeId(int jobTypeId);
}
