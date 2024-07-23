package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.HistoryPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryPaymentRepository extends JpaRepository<HistoryPayment, Integer> {
    // findByUserId
    @Query(value = "SELECT * FROM history_payment WHERE user_id = ?1", nativeQuery = true)
    List<HistoryPayment> findByuid(int userId);
}
