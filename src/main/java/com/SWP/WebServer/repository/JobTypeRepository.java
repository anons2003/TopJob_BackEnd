package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobTypeRepository extends JpaRepository<JobType,Integer> {
    Optional<JobType> findByJobTypeId(int jobTypeId);
}
