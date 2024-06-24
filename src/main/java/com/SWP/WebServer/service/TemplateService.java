package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.TemplateDTO;
import com.SWP.WebServer.entity.Template;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.repository.TemplateRepository;
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
        private TemplateRepository templateRepository;

        public List<Template> getAllTemplates() {
            return templateRepository.findAll();
        }

        public Optional<Template> getTemplateById(Long id) {
            return templateRepository.findById(id);
        }

        public Template createTemplate(TemplateDTO templateDTO, MultipartFile file) {
            String fileName = file.getOriginalFilename();
            String folder = "templates";
            Map<String, Object> uploadResult = cloudinaryService.upload(file, fileName, folder);
            if (uploadResult == null || !uploadResult.containsKey("url")) {
                throw new ApiRequestException("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String imageURL = (String) uploadResult.get("url");

            Template newTemplate = new Template();
            newTemplate.setName(templateDTO.getName());
            newTemplate.setImagerURL(imageURL);
            newTemplate.setTitle(templateDTO.getTitle());

            return templateRepository.save(newTemplate);
        }


        public void deleteTemplate(Long id) {
            templateRepository.deleteById(id);
        }
}
