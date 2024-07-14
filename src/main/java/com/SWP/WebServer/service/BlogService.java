package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.BlogDTO;
import com.SWP.WebServer.entity.Blog;
import com.SWP.WebServer.repository.BlogRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Blog save(Blog blog) {
        return blogRepository.save(blog);
    }

    public BlogDTO createBlog(BlogDTO blogDTO) {
        Blog blog = convertToEntity(blogDTO);
        blog.setCreatedAt(LocalDateTime.now());
        Blog savedBlog = blogRepository.save(blog);
        return convertToDTO(savedBlog);
    }

    public Optional<BlogDTO> updateBlog(Long id, BlogDTO blogDTO) {
        Optional<Blog> existingBlog = blogRepository.findById(id);
        if (existingBlog.isPresent()) {
            Blog blog = existingBlog.get();
            BeanUtils.copyProperties(blogDTO, blog, "id", "createdAt");
            Blog updatedBlog = blogRepository.save(blog);
            return Optional.of(convertToDTO(updatedBlog));
        } else {
            return Optional.empty();
        }
    }
    public void toggleActiveStatus(long id) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            blog.setActive(!blog.isActive());
            blogRepository.save(blog);
        } else {
            throw new IllegalArgumentException("Blog not found with ID: " + id);
        }
    }

    private BlogDTO convertToDTO(Blog blog) {
        BlogDTO blogDTO = new BlogDTO();
        BeanUtils.copyProperties(blog, blogDTO);
        return blogDTO;
    }

    private Blog convertToEntity(BlogDTO blogDTO) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDTO, blog);
        return blog;
    }

}
