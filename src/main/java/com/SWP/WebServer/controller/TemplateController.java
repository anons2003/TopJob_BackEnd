package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.TemplateDTO;
import com.SWP.WebServer.entity.Template;
import com.SWP.WebServer.service.CloudinaryService;
import com.SWP.WebServer.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
public class TemplateController {
    @Autowired
    private TemplateService templateService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/templates")
    public List<Template> getAllTemplates() {
        return templateService.getAllTemplates();
    }

    @GetMapping("/templates/{id}")
    public ResponseEntity<Template> getTemplateById(@PathVariable Long id) {
        Optional<Template> template = templateService.getTemplateById(id);
        return template.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/templates")
    public ResponseEntity<Template> createTemplate(
            @RequestParam("image") MultipartFile file,
            @ModelAttribute TemplateDTO templateDTO) {
        Template newTemplate = templateService.createTemplate(templateDTO, file);
        return ResponseEntity.ok(newTemplate);
    }

    @DeleteMapping("/templates/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}

