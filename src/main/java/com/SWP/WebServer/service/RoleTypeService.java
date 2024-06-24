package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.RoleType;
import com.SWP.WebServer.repository.RoleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleTypeService {
    @Autowired
    private RoleTypeRepository roleTypeRepository;

    public List<RoleType> getAll() {
        return roleTypeRepository.findAll();
    }
}
