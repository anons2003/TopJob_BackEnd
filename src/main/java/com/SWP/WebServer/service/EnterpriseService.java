package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.repository.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    @Autowired
    public EnterpriseService(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    public List<Enterprise> getAllEnterprises() {
        return enterpriseRepository.findAll();
    }

    public Optional<Enterprise> getEnterpriseById(int id) {
        return enterpriseRepository.findById(id);
    }

    public Enterprise saveEnterprise(Enterprise enterprise) {
        return enterpriseRepository.save(enterprise);
    }

    public Enterprise getUserProfile(String userId) {
        return enterpriseRepository.findByUser_Uid(Integer.parseInt(userId));
    }

    public Enterprise getProfileByEid(int userId) {
        return enterpriseRepository.findByEid(userId);
    }

    public void toggleActiveStatus(int id) {
        Optional<Enterprise> optionalEnterprise = enterpriseRepository.findById(id);
        if (optionalEnterprise.isPresent()) {
            Enterprise enterprise = optionalEnterprise.get();
            User user = enterprise.getUser();
            user.setActive(!user.isActive());
            enterpriseRepository.save(enterprise);
        } else {
            throw new IllegalArgumentException("Enterprise not found with ID: " + id);
        }
    }
}