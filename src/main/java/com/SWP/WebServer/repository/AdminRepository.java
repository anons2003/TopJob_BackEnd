package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // You can add custom queries specific to Admin entity if needed
}