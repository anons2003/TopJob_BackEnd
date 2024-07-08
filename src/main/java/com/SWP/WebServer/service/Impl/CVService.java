package com.SWP.WebServer.service.Impl;

import com.SWP.WebServer.dto.AppliedCVDto;
import com.SWP.WebServer.entity.CVApply;

import java.util.List;

public interface CVService {

    CVApply applyCV(
            AppliedCVDto body,
            String userId,
            int eid);

    CVApply reApplyCV(AppliedCVDto body, String userId, int eid);

    String deleteCV(String userId, int eid);

    List<CVApply> GetAllCVByUserId(String userId);

    void uploadResume(String url, String userId,int eid);
}
