package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.RoleType;
import com.SWP.WebServer.repository.RoleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleTypeService {
    @Autowired
    private RoleTypeRepository roleTypeRepository;

    public List<RoleType> getAllUserTypesExcludingAdmin() {
        return roleTypeRepository.findAll().stream()
                .filter(roleType -> !roleType.getRoleTypeName().equalsIgnoreCase("Admin"))
                .collect(Collectors.toList());
    }
}
