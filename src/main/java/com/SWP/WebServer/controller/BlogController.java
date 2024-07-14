package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.Blog;
import com.SWP.WebServer.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/getAllBlogs")
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }
}
