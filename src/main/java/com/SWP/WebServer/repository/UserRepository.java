package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer > {
    User findById(int id);
    User findByEmail(String email);
    User findBySid(String s_id);
    User findByEmailAndSid(String email, String s_id);
}
