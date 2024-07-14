package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.Blog;
import com.SWP.WebServer.repository.BlogRepository;
import com.SWP.WebServer.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/getAllBlogs")
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }
    @GetMapping("/getAllBlogsIsActive")
    public List<Blog> getAllActiveBlogs() {
        return blogRepository.findByIsActiveTrue();
    }
    @GetMapping("/getAllActiveBlogsSortedByCreatedAt")
    public List<Blog> getAllActiveBlogsSortedByCreatedAt() {
        return blogService.getAllActiveBlogsSortedByCreatedAt();
    }
    @GetMapping("/recent")
    public List<Blog> getRecentBlogs() {
        return blogService.getRecentBlogs();
    }

    @GetMapping("/search")
    public List<Blog> searchByTitle(@RequestParam String title) {
        return blogService.searchByTitle(title);
    }

    @GetMapping("/{id}")
    public Blog getBlogById(@PathVariable Long id) {
        return blogService.getBlogById(id);
    }
}
