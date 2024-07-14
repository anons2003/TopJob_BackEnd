package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.AppliedCVDto;
import com.SWP.WebServer.entity.CVApply;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.Job;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.error.DuplicateCVException;
import com.SWP.WebServer.repository.CVRepository;
import com.SWP.WebServer.repository.EnterpriseRepository;
import com.SWP.WebServer.repository.JobPostRepository;
import com.SWP.WebServer.repository.UserRepository;
import com.SWP.WebServer.service.Impl.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    @Override
    public CVApply applyCV(AppliedCVDto body, String userId, int eid, Long jobId) {
        User user = userRepository.findByUid(Integer.parseInt(userId));
        Enterprise enterprise = enterpriseRepository.findByEid(eid);
        Optional<Job> optionalJob = jobPostRepository.findById((jobId));

        if (user == null || enterprise == null) {
            throw new RuntimeException("User or Enterprise not found");
        }

        Job job = optionalJob.orElseThrow(() -> new RuntimeException("Job not found"));

        // Check if a CV already exists for this user and enterprise
        if (cvRepository.existsByUserAndEnterprise(user, enterprise)) {
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
        cvApply.setUser(user);
        cvApply.setEnterprise(enterprise);
        cvApply.setJobId(job);

        return cvRepository.save(cvApply);
    }

    @Override
    public CVApply reApplyCV(AppliedCVDto body, String userId, int eid) {

        CVApply cvApply = cvRepository.findByUser_UidAndEnterprise_Eid(Integer.parseInt(userId),eid);
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
        CVApply cvApply = cvRepository.findByUser_UidAndEnterprise_Eid(Integer.parseInt(userId),eid);

        if (cvApply == null) {
            throw new ApiRequestException("No CV Found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        cvRepository.delete(cvApply);
        return "Delete Completed!";
    }

    @Override
    public List<CVApply> GetAllCVByUserId(String userId) {
        return cvRepository.findAllByUser_Uid(Integer.parseInt(userId));
    }

    @Override
    public void uploadResume(String url, String userId,int eid) {
        CVApply cvApply = cvRepository.findByUser_UidAndEnterprise_Eid(Integer.parseInt(userId),eid);
        cvApply.setResume_url(url);
        cvRepository.save(cvApply);
    }

    @Override
    public List<CVApply> findCVByUid(String userId) {
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(userId));
        int eid = enterprise.getEid();
        return cvRepository.findByEnterprise_Eid(eid);
    }

    @Override
    public String acceptCV(String enId, int uid) {
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(enId));
        int enterpriseId = enterprise.getEid();
        CVApply cvApply = cvRepository.findByUser_UidAndEnterprise_Eid(uid,enterpriseId);
        cvApply.setIsApllied(1);
        cvRepository.save(cvApply);
        return "accepted the cv!";
    }

    @Override
    public String rejectCV(String enId, int uid) {
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(enId));
        int enterpriseId = enterprise.getEid();
        CVApply cvApply = cvRepository.findByUser_UidAndEnterprise_Eid(uid,enterpriseId);
        cvApply.setIsApllied(-1);
        cvRepository.save(cvApply);
        return "reject the cv!";
    }

    @Override
    public String revertCV(String enId, int uid) {
        Enterprise enterprise = enterpriseRepository.findByUser_Uid(Integer.parseInt(enId));
        int enterpriseId = enterprise.getEid();
        CVApply cvApply = cvRepository.findByUser_UidAndEnterprise_Eid(uid,enterpriseId);
        cvApply.setIsApllied(0);
        cvRepository.save(cvApply);
        return "revert to pending!";
    }

}
