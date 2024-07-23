// JobRepository.java
package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<Job, Long> {
    List<Job> findByEnterprise_Eid(int eid);

    List<Job> findByEnterprise_User_Uid(int i);

    List<Job> findByIsActiveTrueOrderByCreatedDateDesc();


}
