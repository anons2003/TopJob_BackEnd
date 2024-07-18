package com.SWP.WebServer.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.SWP.WebServer.entity.Blog;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByIsActiveTrue();
    List<Blog> findByIsActiveTrueOrderByCreatedAtDesc();
    List<Blog> findTop5ByOrderByCreatedAtDesc();

    List<Blog> findByTitleContainingIgnoreCase(String title);
}