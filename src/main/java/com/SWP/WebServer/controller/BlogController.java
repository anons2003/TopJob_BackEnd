package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.BlogDTO;
import com.SWP.WebServer.entity.Blog;
import com.SWP.WebServer.repository.BlogRepository;
import com.SWP.WebServer.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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


    @PostMapping("/create")
    public ResponseEntity<BlogDTO> createBlog(@RequestBody BlogDTO blogDTO) {
        BlogDTO createdBlog = blogService.createBlog(blogDTO);
        return new ResponseEntity<>(createdBlog, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<BlogDTO> updateBlog(@PathVariable Long id, @RequestBody BlogDTO blogDTO) {
        Optional<BlogDTO> updatedBlog = blogService.updateBlog(id, blogDTO);
        return updatedBlog.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PatchMapping("/toggle-active/{id}")
    public ResponseEntity<?> toggleActive(@PathVariable int id) {
        try {
            blogService.toggleActiveStatus(id);
            return ResponseEntity.ok("Blog active status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
