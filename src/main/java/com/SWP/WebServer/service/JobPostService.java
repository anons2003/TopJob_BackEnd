// JobService.java
package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.JobPostDTO;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.entity.JobType;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.repository.EnterpriseRepository;
import com.SWP.WebServer.repository.JobPostRepository;
import com.SWP.WebServer.repository.JobTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private JobTypeRepository jobTypeRepository;


    // Phương thức lưu một bài đăng công việc
    public Job saveJob(String userId, JobPostDTO body) {
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(userId));
        int jobTypeId = body.getJobTypeId();
        Job job = new Job();
        job.setActive(false);
        job.setJobCategory(body.getJobCategory());
        job.setDescription(body.getDescription());
        job.setTitle(body.getTitle());
        job.setSkills(body.getSkills());
        job.setQualifications(body.getQualifications());
        job.setExperience(body.getExperience());
        job.setIndustry(body.getIndustry());
        job.setEnterprise(enterprise);
        job.setMaxSalary(body.getMaxSalary());
        job.setMinSalary(body.getMinSalary());
        job.setSalaryType(body.getSalaryType());
        job.setAddress(body.getAddress());
        job.setCountry(body.getCountry());
        job.setState(body.getState());

        JobType jobType = jobTypeRepository.findByJobTypeId(jobTypeId)
                .orElseThrow(() -> new ApiRequestException("Job Type not found", HttpStatus.BAD_REQUEST));
        job.setJobType(jobType);
        return jobPostRepository.save(job);
    }

    public List <Job> findJobsByUid(String eid){
        return jobPostRepository.findByEnterprise_User_Uid(Integer.parseInt(eid));
    }

    // Phương thức đếm tổng số bài đăng công việc
    public long countJobs() {
        return jobPostRepository.count();
    }
    // Lấy danh sách công việc
    public List<Job> getAllJobs() {
        return jobPostRepository.findAll();
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
}