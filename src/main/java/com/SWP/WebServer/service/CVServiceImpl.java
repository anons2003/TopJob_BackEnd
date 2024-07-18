package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.AppliedCVDto;
import com.SWP.WebServer.entity.*;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.error.DuplicateCVException;
import com.SWP.WebServer.repository.*;
import com.SWP.WebServer.service.Impl.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CVServiceImpl implements CVService {
    @Autowired
    CVRepository cvRepository;

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JobPostRepository jobPostRepository;

    @Autowired
    JobSeekerRepository jobSeekerRepository;

    @Override
    public CVApply applyCV(AppliedCVDto body, String userId, int eid, Long jobId) {
        JobSeeker jobSeeker = jobSeekerRepository.findByUser_Uid(Integer.parseInt(userId));
        Enterprise enterprise = enterpriseRepository.findByEid(eid);
        Optional<Job> optionalJob = jobPostRepository.findById((jobId));

        if (jobSeeker == null || enterprise == null) {
            throw new RuntimeException("User or Enterprise not found");
        }

        Job job = optionalJob.orElseThrow(() -> new RuntimeException("Job not found"));

        // Check if a CV already exists for this user and enterprise
        if (cvRepository.existsByJobSeekerAndEnterprise(jobSeeker, enterprise)) {
            throw new DuplicateCVException("A CV for this user and enterprise already exists");
        }


        CVApply cvApply = new CVApply();
        cvApply.setFull_name(body.getFull_name());
        cvApply.setEmail(body.getEmail());
        cvApply.setPhone(body.getPhone());
        cvApply.setJob(body.getJob());
        cvApply.setJobType(body.getJobType());
        cvApply.setDescription(body.getDescription());
        cvApply.setIsApllied(0);
        cvApply.setResume_url(body.getResume_url());
        cvApply.setJobSeeker(jobSeeker);
        cvApply.setEnterprise(enterprise);
        cvApply.setJobId(job);

        return cvRepository.save(cvApply);
    }

    @Override
    public CVApply reApplyCV(AppliedCVDto body, String userId, int eid) {

        CVApply cvApply = cvRepository.findByJobSeeker_User_UidAndEnterprise_Eid(Integer.parseInt(userId),eid);
        cvApply.setFull_name(body.getFull_name());
        cvApply.setEmail(body.getEmail());
        cvApply.setPhone(body.getPhone());
        cvApply.setJob(body.getJob());
        cvApply.setJobType(body.getJobType());
        cvApply.setDescription(body.getDescription());
        cvApply.setResume_url(body.getResume_url());

        return cvRepository.save(cvApply);
    }

    @Override
    public String deleteCV(String userId, int eid) {
        CVApply cvApply = cvRepository.findByJobSeeker_User_UidAndEnterprise_Eid(Integer.parseInt(userId),eid);

        if (cvApply == null) {
            throw new ApiRequestException("No CV Found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        cvRepository.delete(cvApply);
        return "Delete Completed!";
    }

    @Override
    public List<CVApply> GetAllCVByUserId(String userId) {
        return cvRepository.findAllByJobSeeker_User_Uid(Integer.parseInt(userId));
    }

    @Override
    public void uploadResume(String url, String userId,int eid) {
        CVApply cvApply = cvRepository.findByJobSeeker_User_UidAndEnterprise_Eid(Integer.parseInt(userId),eid);
        cvApply.setResume_url(url);
        cvRepository.save(cvApply);
    }

    @Override
    public List<CVApply> findCVByUid(String userId) {
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(userId));
        int eid = enterprise.getEid();
        return cvRepository.findByEnterprise_Eid(eid);
    }

    public void markForDeletion(CVApply cvApply, int daysUntilDeletion){
//        Use for Scheduling days
//        cvApply.setDeletionTime(LocalDateTime.now().plusDays(daysUntilDeletion));

//        Use for testing in minutes
        cvApply.setDeletionTime(LocalDateTime.now().plusMinutes(daysUntilDeletion));
    }

    @Override
    public String acceptCV(String enId, int uid) {
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(enId));
        int enterpriseId = enterprise.getEid();
        CVApply cvApply = cvRepository.findByJobSeeker_User_UidAndEnterprise_Eid(uid,enterpriseId);
        cvApply.setIsApllied(1);
        markForDeletion(cvApply,1);
        cvRepository.save(cvApply);
        return "accepted the cv!";
    }

    @Override
    public String rejectCV(String enId, int uid) {
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(enId));
        int enterpriseId = enterprise.getEid();
        CVApply cvApply = cvRepository.findByJobSeeker_User_UidAndEnterprise_Eid(uid,enterpriseId);
        cvApply.setIsApllied(-1);
        markForDeletion(cvApply,1);
        cvRepository.save(cvApply);
        return "reject the cv!";
    }
    // Nho kiem tra xem co cai @EnableScheduling trong WebServerSwpApplication thi moi chay duoc
    @Scheduled(cron = "*/30 * * * * *") // Run every 30 seconds for quicker testing
//    @Scheduled(cron = "0 0 * * * *") // Run every hour
    public void deleteMarkedCVs(){
//        System.out.println("I am scanning for deletion");
//        It Works!
        List<CVApply> cvApplies = cvRepository.findAll();
        LocalDateTime now  = LocalDateTime.now();
        for(CVApply cv : cvApplies){
            if(cv.getDeletionTime() != null && now.isAfter(cv.getDeletionTime())){
                cvRepository.delete(cv);
//                System.out.println("Done!");
            }
        }
    }

    @Override
    public String revertCV(String enId, int uid) {
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(enId));
        int enterpriseId = enterprise.getEid();
        CVApply cvApply = cvRepository.findByJobSeeker_User_UidAndEnterprise_Eid(uid,enterpriseId);
        cvApply.setIsApllied(0);
        cvRepository.save(cvApply);
        return "revert to pending!";
    }

}
