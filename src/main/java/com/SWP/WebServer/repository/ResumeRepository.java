package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    Resume findByuIdAndTemplateName(int uId, String templateName);
}
