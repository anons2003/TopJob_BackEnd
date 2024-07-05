package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.CVApply;
import com.SWP.WebServer.entity.Enterprise;
import com.SWP.WebServer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CVRepository extends JpaRepository<CVApply, Integer> {
    CVApply findByCvId(int cv_id);

    @Query("SELECT cv FROM CVApply cv WHERE cv.user.uid = :user_id")
    List<CVApply> findAllByUser_Uid(@Param("user_id") int userId);

    CVApply findByUser_UidAndEnterprise_Eid(int userId,int eid);
    boolean existsByUserAndEnterprise(User user, Enterprise enterprise);
}
