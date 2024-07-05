package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.AppliedCVDto;
import com.SWP.WebServer.entity.CVApply;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.exception.DuplicateCVException;
import com.SWP.WebServer.repository.CVRepository;
import com.SWP.WebServer.repository.EnterpriseRepository;
import com.SWP.WebServer.repository.UserRepository;
import com.SWP.WebServer.service.Impl.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CVServiceImpl implements CVService {
    @Autowired
    CVRepository cvRepository;

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public CVApply applyCV(AppliedCVDto body, String userId, int eid) {
        User user = userRepository.findByUid(Integer.parseInt(userId));
        Enterprise enterprise = enterpriseRepository.findByEid(eid);

        if (user == null || enterprise == null) {
            throw new RuntimeException("User or Enterprise not found");
        }

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
        cvApply.setIsApllied((byte) 0);
        cvApply.setUser(user);
        cvApply.setEnterprise(enterprise);

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


}
