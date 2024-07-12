package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.ResumeRequestDTO;
import com.SWP.WebServer.entity.Resume;
import com.SWP.WebServer.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @GetMapping
    public List<Resume> getAllResumes() {
        return resumeService.getAllResumes();
    }

    @GetMapping("/{id}")
    public Resume getResumeById(@PathVariable int id) {
        return resumeService.getResumeById(id);
    }

    @GetMapping("/user/{uId}/resume/{templateName}")
    public ResponseEntity<Resume> getResumeByUserIdAndTemplateName(@PathVariable int uId, @PathVariable String templateName) {
        System.out.println("Received uid: " + uId + ", templateName: " + templateName);
        Resume resume = resumeService.getResumeByUserIdAndTemplateName(uId, templateName);
        if (resume == null) {
            // Create a new resume if it does not exist
            resume = new Resume();
            resume.setUId(uId);
            resume.setTemplateName(templateName);
            // Set other properties of resume if needed
            resume = resumeService.saveResume(resume);
        }
        return ResponseEntity.ok(resume);
    }

//    @PostMapping("/user/{uId}/resume/{templateName}")
//    public ResponseEntity<Resume> createResumeByUserIdAndTemplateName(
//            @PathVariable int uId, @PathVariable String templateName, @RequestBody ResumeRequestDTO resumeRequestDTO) {
//        Resume resume = resumeService.createResume(uId, templateName, resumeRequestDTO);
//        return ResponseEntity.ok(resume);
//    }

    @PatchMapping("/user/{uId}/resume/{templateName}")
    public ResponseEntity<Resume> updateResumeByUserIdAndTemplateName(
            @PathVariable int uId, @PathVariable String templateName, @RequestBody ResumeRequestDTO resumeRequestDTO) {
        Resume resume = resumeService.updateResume(uId, templateName, resumeRequestDTO);
        return ResponseEntity.ok(resume);
    }


    @DeleteMapping("/{id}")
    public void deleteResume(@PathVariable int id) {
        resumeService.deleteResume(id);
    }
}