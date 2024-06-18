package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.AppliedCVDto;
import com.SWP.WebServer.entity.CVApply;

public interface CVService {

    CVApply applyCV(
            AppliedCVDto body,
            String userId,
            int eid);
}
