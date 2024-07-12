package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.ResumeRequestDTO;
import com.SWP.WebServer.entity.Resume;
import com.SWP.WebServer.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeUpdateHelper resumeUpdateHelper;

    @Autowired
    public ResumeService(ResumeRepository resumeRepository, ResumeUpdateHelper resumeUpdateHelper) {
        this.resumeRepository = resumeRepository;
        this.resumeUpdateHelper = resumeUpdateHelper;
    }

    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }

    public Resume getResumeById(int id) {
        return resumeRepository.findById(id).orElse(null);
    }

    public Resume getResumeByUserIdAndTemplateName(int uId, String templateName) {
        return resumeRepository.findByuIdAndTemplateName(uId, templateName);
    }

    public Resume saveResume(Resume resume) {
        return resumeRepository.save(resume);
    }



    public Resume updateResume(int uId, String templateName, ResumeRequestDTO resumeRequestDTO) {
        Resume existingResume = getResumeByUserIdAndTemplateName(uId, templateName);
        if (existingResume != null) {
            updateResumeAttributes(existingResume, resumeRequestDTO);
            processLists(existingResume, resumeRequestDTO);
            return saveResume(existingResume);
        } else {
            throw new RuntimeException("Resume not found");
        }
    }

    public void deleteResume(int id) {
        resumeRepository.deleteById(id);
    }



    private void updateResumeAttributes(Resume resume, ResumeRequestDTO resumeRequestDTO) {
        resume.setFullname(resumeRequestDTO.getFullname());
        resume.setAddress(resumeRequestDTO.getAddress());
        resume.setMobile(resumeRequestDTO.getMobile());
        resume.setEmail(resumeRequestDTO.getEmail());
        resume.setPersonalDescription(resumeRequestDTO.getPersonalDescription());
        resume.setProfessionalTitle(resumeRequestDTO.getProfessionalTitle());
        resume.setRefererName(resumeRequestDTO.getRefererName());
        resume.setRefererRole(resumeRequestDTO.getRefererRole());
        resume.setWebsite(resumeRequestDTO.getWebsite());
        resume.setUserProfilePic(resumeRequestDTO.getUserProfilePic());
        resume.setImageURL(resumeRequestDTO.getImageURL());
    }

    private void processLists(Resume resume, ResumeRequestDTO resumeRequestDTO) {
        resumeUpdateHelper.updateSkills(resume, resumeRequestDTO.getSkills());
        resumeUpdateHelper.updateEducations(resume, resumeRequestDTO.getEducation());
        resumeUpdateHelper.updateExperiences(resume, resumeRequestDTO.getExperiences());
    }
}