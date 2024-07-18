package com.SWP.WebServer.service;


import com.SWP.WebServer.entity.JobSeeker;
import com.SWP.WebServer.repository.JobSeekerRepository;
import com.SWP.WebServer.repository.RoleTypeRepository;
import com.SWP.WebServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobSeekerService {

    private final JobSeekerRepository jobSeekerRepository;
    @Autowired
    private RoleTypeRepository roleTypeRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public JobSeekerService(JobSeekerRepository jobSeekerRepository) {
        this.jobSeekerRepository = jobSeekerRepository;
    }

    public List<JobSeeker> getAllJobSeekers() {
        return jobSeekerRepository.findAll();
    }

    public Optional<JobSeeker> getJobSeekerById(int id) {
        return jobSeekerRepository.findById(id);
    }

    public JobSeeker saveJobSeeker(JobSeeker jobSeeker) {
        return jobSeekerRepository.save(jobSeeker);
    }

}