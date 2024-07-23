// JobService.java
package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.BookmarkDTO;
import com.SWP.WebServer.dto.JobDTO;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.entity.JobCategory;
import com.SWP.WebServer.entity.JobType;
import com.SWP.WebServer.exception.EntityNotFoundException;
import com.SWP.WebServer.repository.EnterpriseRepository;
import com.SWP.WebServer.repository.JobCategoryRepository;
import com.SWP.WebServer.repository.JobPostRepository;
import com.SWP.WebServer.repository.JobTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private JobTypeRepository jobTypeRepository;

    @Autowired
    private JobCategoryRepository jobCategoryRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private NotificationService notificationService;

    // Phương thức lưu một bài đăng công việc
//    public Job saveJob(Job job) {
//        return jobPostRepository.save(job);
//    }

    //get job theo tung enterpriseId
    public List<Job> getJobsByEnterpriseId(int eid) {
        return jobPostRepository.findByEnterprise_Eid(eid);
    }

    //    public Job saveJob(Job job) {
//        if (job.getJobTypeEntity() != null) {
//            JobType jobType = jobTypeRepository.findById(job.getJobTypeEntity().getJobTypeId()).orElse(null);
//            job.setJobTypeEntity(jobType);
//        }
//
//        if (job.getJobCategoryEntity() != null) {
//            JobCategory jobCategory = jobCategoryRepository.findById(job.getJobCategoryEntity().getJobCategoryId()).orElse(null);
//            job.setJobCategoryEntity(jobCategory);
//        }
//
//        if (job.getEnterprise() != null) {
//            Enterprise enterprise = enterpriseRepository.findById(job.getEnterprise().getEid()).orElse(null);
//            job.setEnterprise(enterprise);
//        }
//
//        return jobPostRepository.save(job);
//    }
//
    // Phương thức đếm tổng số bài đăng công việc
    public long countJobs() {
        return jobPostRepository.count();

    }
    public Job saveJob(Job job) {
        if (job.getJobTypeEntity() != null) {
            JobType jobType = jobTypeRepository.findById(job.getJobTypeEntity().getJobTypeId()).orElse(null);
            job.setJobTypeEntity(jobType);
        }

        if (job.getJobCategoryEntity() != null) {
            JobCategory jobCategory = jobCategoryRepository.findById(job.getJobCategoryEntity().getJobCategoryId()).orElse(null);
            job.setJobCategoryEntity(jobCategory);
        }

        if (job.getEnterprise() != null) {
            Enterprise enterprise = enterpriseRepository.findById(job.getEnterprise().getEid()).orElse(null);
            job.setEnterprise(enterprise);
        }

        Job savedJob = jobPostRepository.save(job);
        return savedJob;
    }

    // Lấy danh sách công việc
//    public List<Job> getAllJobs() {
//        return jobPostRepository.findAll();
//    }

    //lay job active moi nhat
    public List<Job> getAllActiveJobs() {
        return jobPostRepository.findByIsActiveTrueOrderByCreatedDateDesc();
    }



    // Lấy danh sách công việc 2.0
    public List<JobDTO> getAllJobDTOs() {
        return getAllActiveJobs().stream()
                .filter(Job::isActive) // Only include active jobs
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public JobDTO convertToDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setSalaryType(job.getSalaryType());
        dto.setMinSalary(job.getMinSalary());
        dto.setMaxSalary(job.getMaxSalary());
        dto.setSkills(job.getSkills());
        dto.setQualifications(job.getQualifications());
        dto.setExperience(job.getExperience());
        dto.setIndustry(job.getIndustry());
        dto.setAddress(job.getAddress());
        dto.setCountry(job.getCountry());
        dto.setState(job.getState());
        dto.setCreatedAt(job.getCreatedDate());
        dto.setUpdatedAt(job.getUpdatedAt());
        dto.setActive(job.isActive());
        dto.setCity(job.getCity());
        if (job.getEnterprise() != null) {
            dto.setEnterpriseName(job.getEnterprise().getEnterprise_name());
            dto.setAvatarUrl(job.getEnterprise().getAvatar_url());
            dto.setEnterpriseId(job.getEnterprise().getEid());

        }


        if (job.getJobTypeEntity() != null) {
            dto.setJobTypeName(job.getJobTypeEntity().getJobTypeName());
            dto.setJobTypeId(job.getJobTypeEntity().getJobTypeId());

        }


        if (job.getJobCategoryEntity() != null) {
            dto.setJobCategoryName(job.getJobCategoryEntity().getJobCategoryName());
            dto.setJobCategoryId(job.getJobCategoryEntity().getJobCategoryId());
        }
        List<BookmarkDTO> bookmarkDTOs = job.getBookmarks().stream()
                .map(bookmark -> new BookmarkDTO(
                        bookmark.getId(),
                        bookmark.getJobId().getId(),
                        bookmark.getJobSeekers().getJid(),
                        bookmark.getIsBookmarked()
                ))
                .collect(Collectors.toList());
        dto.setBookmarks(bookmarkDTOs);
        return dto;
    }

    // Tìm công việc bằng ID
    public Optional<Job> getJobById(Long id) {
        // Triển khai logic tìm công việc theo ID từ cơ sở dữ liệu
        return jobPostRepository.findById(id);
    }

    public void toggleActiveStatus(Long id) {
        Optional<Job> optionalJob = jobPostRepository.findById(id);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            job.setActive(!job.isActive());
            jobPostRepository.save(job);
        } else {
            throw new IllegalArgumentException("Job not found with ID: " + id);
        }
    }

    public List<Job> findJobsByUid(String eid) {
        return jobPostRepository.findByEnterprise_User_Uid(Integer.parseInt(eid));
    }

    public List<Job> getAllsJobs() {
        return jobPostRepository.findAll();
    }

    // delete.java
    public void deleteJob(Long jobId) {
        Optional<Job> jobOptional = jobPostRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();

            // Manually delete related records if necessary
            job.getBookmarks().clear();
            job.getCvApplies().clear();
            jobPostRepository.delete(job);
        } else {
            throw new IllegalArgumentException("Job not found with ID: " + jobId);
        }
    }


    // update job
    public Job updateJob(Long id, JobDTO jobDTO) {
        Optional<Job> jobOptional = jobPostRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(jobDTO.getTitle());
            job.setDescription(jobDTO.getDescription());
            job.setSalaryType(jobDTO.getSalaryType());
            job.setMinSalary(jobDTO.getMinSalary());
            job.setMaxSalary(jobDTO.getMaxSalary());
            job.setSkills(jobDTO.getSkills());
            job.setQualifications(jobDTO.getQualifications());
            job.setExperience(jobDTO.getExperience());
            job.setIndustry(jobDTO.getIndustry());
            job.setAddress(jobDTO.getAddress());
            job.setCountry(jobDTO.getCountry());
            job.setState(jobDTO.getState());
            job.setCity(jobDTO.getCity());
            if (jobDTO.getJobTypeId() != null) {
                JobType jobType = jobTypeRepository.findById(jobDTO.getJobTypeId()).orElse(null);
                job.setJobTypeEntity(jobType);

            }

            if (jobDTO.getJobCategoryId() != null) {
                JobCategory jobCategory = jobCategoryRepository.findById(jobDTO.getJobCategoryId()).orElse(null);
                job.setJobCategoryEntity(jobCategory);
            }

            if (jobDTO.getEnterpriseId() != null) {
                Enterprise enterprise = enterpriseRepository.findById(jobDTO.getEnterpriseId()).orElse(null);
                job.setEnterprise(enterprise);
            }

            job = jobPostRepository.save(job);
            return job;
        } else {
            throw new EntityNotFoundException("Job not found");
        }
    }

}
