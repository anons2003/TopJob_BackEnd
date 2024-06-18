package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleTypeRepository extends JpaRepository<RoleType,Integer> {
    Optional<RoleType> findByRoleTypeId(int roleTypeId);
}
