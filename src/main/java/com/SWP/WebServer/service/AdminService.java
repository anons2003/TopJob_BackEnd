package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.Admin;
import com.SWP.WebServer.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(int id) {
        return adminRepository.findById((long) id);
    }

    public Admin saveAdmin(Admin admin) {
        // Additional logic can be added here if needed before saving
        return adminRepository.save(admin);
    }

    public void deleteAdminById(int id) {
        adminRepository.deleteById((long) id);
    }
}