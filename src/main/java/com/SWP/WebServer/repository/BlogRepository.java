package com.SWP.WebServer.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.SWP.WebServer.entity.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}
