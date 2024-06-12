// JobRepository.java
package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostRepository extends JpaRepository<Job, Long> {
}
