package com.SWP.WebServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.SWP.WebServer.entity.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeRequestDTO {
    private int uId;
    private String templateName;
    private String timeStamp;
    private String userProfilePic;

    // FormData fields
    private String fullname;
    private String professionalTitle;
    private String personalDescription;
    private String refererName;
    private String refererRole;
    private String mobile;
    private String email;
    private String website;
    private String address;

    private String imageURL;
    private List<EducationDTO> education;
    private List<ExperienceDTO> experiences;
    private List<SkillDTO> skills;

    public ResumeRequestDTO(Resume resume) {
        this.uId = resume.getUId();
        this.templateName = resume.getTemplateName();
        this.timeStamp = resume.getTimeStamp();
        this.userProfilePic = resume.getUserProfilePic();
        // Map other fields if needed

        // Map Education list to EducationDTO list
        if (resume.getEducation() != null) {
            this.education = new ArrayList<>();
            for (Education education : resume.getEducation()) {
                this.education.add(new EducationDTO(education));
            }
        }

        // Map Experience list to ExperienceDTO list
        if (resume.getExperiences() != null) {
            this.experiences = new ArrayList<>();
            for (Experience experience : resume.getExperiences()) {
                this.experiences.add(new ExperienceDTO(experience));
            }
        }

        // Map Skill list to SkillDTO list
        if (resume.getSkills() != null) {
            this.skills = new ArrayList<>();
            for (Skill skill : resume.getSkills()) {
                this.skills.add(new SkillDTO(skill));
            }
        }
    }

    public Resume toEntity() {
        Resume resume = new Resume();
        resume.setUId(this.uId);
        resume.setTemplateName(this.templateName);
        resume.setTimeStamp(this.timeStamp);
        resume.setUserProfilePic(this.userProfilePic);

        // Map EducationDTO list to Education list in Resume
        if (this.education != null) {
            List<Education> educationList = new ArrayList<>();
            for (EducationDTO educationDTO : this.education) {
                Education education = new Education();
                education.setMajor(educationDTO.getMajor());
                education.setUniversity(educationDTO.getUniversity());
                // Set other fields as needed
                educationList.add(education);
            }
            resume.setEducation(educationList);
        }

        // Map ExperienceDTO list to Experience list in Resume
        if (this.experiences != null) {
            List<Experience> experienceList = new ArrayList<>();
            for (ExperienceDTO experienceDTO : this.experiences) {
                Experience experience = new Experience();
                experience.setYear(experienceDTO.getYear());
                experience.setTitle(experienceDTO.getTitle());
                experience.setCompanyAndLocation(experienceDTO.getCompanyAndLocation());
                experience.setDescription(experienceDTO.getDescription());
                // Set other fields as needed
                experienceList.add(experience);
            }
            resume.setExperiences(experienceList);
        }

        // Map SkillDTO list to Skill list in Resume
        if (this.skills != null) {
            List<Skill> skillList = new ArrayList<>();
            for (SkillDTO skillDTO : this.skills) {
                Skill skill = new Skill();
                skill.setTitle(skillDTO.getTitle());
                skill.setPercentage(skillDTO.getPercentage());
                // Set other fields as needed
                skillList.add(skill);
            }
            resume.setSkills(skillList);
        }

        return resume;
    }
}
