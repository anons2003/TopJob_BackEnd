package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.AppliedCVDto;
import com.SWP.WebServer.entity.CVApply;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.exception.DuplicateCVException;
import com.SWP.WebServer.repository.CVRepository;
import com.SWP.WebServer.repository.EnterpriseRepository;
import com.SWP.WebServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
