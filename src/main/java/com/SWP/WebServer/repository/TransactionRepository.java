package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueBetweenDates(LocalDate startDate, LocalDate endDate);




}
