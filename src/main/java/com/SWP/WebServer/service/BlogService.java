package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.Blog;
import com.SWP.WebServer.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
    public List<Blog> getAllActiveBlogsSortedByCreatedAt() {
        return blogRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }

    public List<Blog> getRecentBlogs() {
        return blogRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public List<Blog> searchByTitle(String title) {
        return blogRepository.findByTitleContainingIgnoreCase(title);
    }
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

}
