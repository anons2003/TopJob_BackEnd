package com.SWP.WebServer.service;


import com.SWP.WebServer.dto.TemplateDTO;
import com.SWP.WebServer.entity.Template;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.repository.TemplateRepository;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TemplateService {
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private TemplateRepository templateRepository;

    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Optional<Template> getTemplateById(Long id) {
        return templateRepository.findById(id);
    }

    public Template createTemplate(TemplateDTO templateDTO, MultipartFile file) {
        // Lấy tên file gốc
        String fileName = file.getOriginalFilename();

        // Kiểm tra nếu tên file không null và có chứa dấu "."
        if (fileName != null && fileName.contains(".")) {
            // Lấy phần tên file trước dấu "."
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        String folder = "templates";
        Map<String, Object> uploadResult = cloudinaryService.upload(file, fileName, folder);
        if (uploadResult == null || !uploadResult.containsKey("url")) {
            throw new ApiRequestException("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String imageURL = (String) uploadResult.get("url");

        Template newTemplate = new Template();
        newTemplate.setName(templateDTO.getName());
        newTemplate.setImageURL(imageURL);
        newTemplate.setTitle(templateDTO.getTitle());

        return templateRepository.save(newTemplate);
    }


    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }

    //testing xoá cả ảnh trên cloudinary nhưng mà không được

//    @Transactional
//    public void deleteTemplate(Long id) {
//        Template template = templateRepository.findById(id)
//                .orElseThrow(() -> new ApiRequestException("Template not found with id " + id,HttpStatus.INTERNAL_SERVER_ERROR));
//
//        String publicId = extractPublicIdFromUrl(template.getImageURL());
//
//        try {
//            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to delete image from Cloudinary", e);
//        }
//
//        templateRepository.deleteById(id);
//    }
//
//    private String extractPublicIdFromUrl(String url) {
//        // Logic to extract publicId from the URL
//        // Example: "http://res.cloudinary.com/dz9kynjwb/image/upload/v1719505975/templates/Template1.jpg"
//        // Should return: "templates/Template1"
//        String[] parts = url.split("/");
//        String publicIdWithExtension = parts[parts.length - 1];
//        return publicIdWithExtension.split("\\.")[0];
//    }
}