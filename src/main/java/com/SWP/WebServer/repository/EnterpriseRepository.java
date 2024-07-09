package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.Enterprise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Integer> {
    Enterprise findByEid(int e_id);
    Enterprise findByUser_Uid(int u_id);
}
