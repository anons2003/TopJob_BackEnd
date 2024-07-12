package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.EducationDTO;
import com.SWP.WebServer.dto.ExperienceDTO;
import com.SWP.WebServer.dto.SkillDTO;
import com.SWP.WebServer.entity.Education;
import com.SWP.WebServer.entity.Experience;
import com.SWP.WebServer.entity.Resume;
import com.SWP.WebServer.entity.Skill;
import com.SWP.WebServer.repository.EducationRepository;
import com.SWP.WebServer.repository.ExperienceRepository;
import com.SWP.WebServer.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResumeUpdateHelper {

    private final SkillRepository skillRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;

    @Autowired
    public ResumeUpdateHelper(SkillRepository skillRepository, EducationRepository educationRepository,
                              ExperienceRepository experienceRepository) {
        this.skillRepository = skillRepository;
        this.educationRepository = educationRepository;
        this.experienceRepository = experienceRepository;
    }

    public void updateSkills(Resume resume, List<SkillDTO> skillDTOList) {
        List<Skill> existingSkills = resume.getSkills();
        List<Integer> newSkillIds = skillDTOList.stream()
                .map(SkillDTO::getId)
                .collect(Collectors.toList());

        // Find skills that need to be removed based on ID
        List<Skill> skillsToRemove = existingSkills.stream()
                .filter(skill -> !newSkillIds.contains(skill.getId()))
                .collect(Collectors.toList());

        // Remove skills from the resume and database
        for (Skill skill : skillsToRemove) {
            resume.getSkills().remove(skill);
            skillRepository.delete(skill); // Remove from database
        }

        List<Skill> updatedSkills = mapSkillDTOList(skillDTOList, resume);
        resume.setSkills(updatedSkills);
    }

    public List<Skill> mapSkillDTOList(List<SkillDTO> skillDTOList, Resume resume) {
        Map<Integer, Skill> existingSkillMap = resume.getSkills().stream()
                .collect(Collectors.toMap(Skill::getId, skill -> skill, (existing, replacement) -> existing));

        return skillDTOList.stream()
                .map(skillDTO -> {
                    Skill skill;
                    if (existingSkillMap.containsKey(skillDTO.getId())) {
                        skill = existingSkillMap.get(skillDTO.getId());
                        skill.setPercentage(skillDTO.getPercentage());
                        skill.setTitle(skillDTO.getTitle());
                    } else {
                        skill = new Skill();
                        skill.setTitle(skillDTO.getTitle());
                        skill.setPercentage(skillDTO.getPercentage());
                        skill.setResume(resume); // Set the resume reference
                    }
                    System.out.println("Mapped Skill: " + skill);
                    return skill;
                })
                .collect(Collectors.toList());
    }

    public void updateEducations(Resume resume, List<EducationDTO> educationDTOList) {
        List<Education> existingEducations = resume.getEducation();
        List<Integer> newEducationIds = educationDTOList.stream()
                .map(EducationDTO::getId)
                .collect(Collectors.toList());

        // Find educations that need to be removed based on ID
        List<Education> educationsToRemove = existingEducations.stream()
                .filter(education -> !newEducationIds.contains(education.getId()))
                .collect(Collectors.toList());

        // Remove educations from the resume and database
        for (Education education : educationsToRemove) {
            resume.getEducation().remove(education);
            educationRepository.delete(education); // Remove from database
        }

        List<Education> updatedEducations = mapEducationDTOList(educationDTOList, resume);
        resume.setEducation(updatedEducations);
    }

    public List<Education> mapEducationDTOList(List<EducationDTO> educationDTOList, Resume resume) {
        Map<Integer, Education> existingEducationMap = resume.getEducation().stream()
                .collect(Collectors.toMap(Education::getId, education -> education, (existing, replacement) -> existing));

        return educationDTOList.stream()
                .map(eduDTO -> {
                    Education edu;
                    if (existingEducationMap.containsKey(eduDTO.getId())) {
                        edu = existingEducationMap.get(eduDTO.getId());
                        edu.setUniversity(eduDTO.getUniversity());
                        edu.setMajor(eduDTO.getMajor());
                    } else {
                        edu = new Education();
                        edu.setMajor(eduDTO.getMajor());
                        edu.setUniversity(eduDTO.getUniversity());
                        edu.setResume(resume); // Set the resume reference
                    }
                    return edu;
                })
                .collect(Collectors.toList());
    }

    public void updateExperiences(Resume resume, List<ExperienceDTO> experienceDTOList) {
        List<Experience> existingExperiences = resume.getExperiences();
        List<Integer> newExperienceIds = experienceDTOList.stream()
                .map(ExperienceDTO::getId)
                .collect(Collectors.toList());

        // Find experiences that need to be removed based on ID
        List<Experience> experiencesToRemove = existingExperiences.stream()
                .filter(experience -> !newExperienceIds.contains(experience.getId()))
                .collect(Collectors.toList());

        // Remove experiences from the resume and database
        for (Experience experience : experiencesToRemove) {
            resume.getExperiences().remove(experience);
            experienceRepository.delete(experience); // Remove from database
        }

        List<Experience> updatedExperiences = mapExperienceDTOList(experienceDTOList, resume);
        resume.setExperiences(updatedExperiences);
    }

    public List<Experience> mapExperienceDTOList(List<ExperienceDTO> experienceDTOList, Resume resume) {
        Map<Integer, Experience> existingExperienceMap = resume.getExperiences().stream()
                .collect(Collectors.toMap(Experience::getId, experience -> experience, (existing, replacement) -> existing));

        return experienceDTOList.stream()
                .map(expDTO -> {
                    Experience exp;
                    if (existingExperienceMap.containsKey(expDTO.getId())) {
                        exp = existingExperienceMap.get(expDTO.getId());
                        exp.setYear(expDTO.getYear());
                        exp.setCompanyAndLocation(expDTO.getCompanyAndLocation());
                        exp.setDescription(expDTO.getDescription());
                        exp.setTitle(expDTO.getTitle());
                    } else {
                        exp = new Experience();
                        exp.setYear(expDTO.getYear());
                        exp.setTitle(expDTO.getTitle());
                        exp.setCompanyAndLocation(expDTO.getCompanyAndLocation());
                        exp.setDescription(expDTO.getDescription());
                        exp.setResume(resume); // Set the resume reference
                    }
                    return exp;
                })
                .collect(Collectors.toList());
    }
}