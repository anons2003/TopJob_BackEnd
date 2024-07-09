package com.SWP.WebServer.repository;

import com.SWP.WebServer.dto.MonthlyIncomeDTO;
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

    @Query("SELECT new com.SWP.WebServer.dto.MonthlyIncomeDTO(MONTH(t.transactionDate), SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE YEAR(t.transactionDate) = :year " +
            "GROUP BY MONTH(t.transactionDate) " +
            "ORDER BY MONTH(t.transactionDate)")
    List<MonthlyIncomeDTO> findMonthlyIncomeByYear(int year);
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE DATE(t.transactionDate) = :date")
    BigDecimal calculateDailyRevenue(@Param("date") LocalDate date);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE WEEK(t.transactionDate) = WEEK(:date) AND YEAR(t.transactionDate) = YEAR(:date)")
    BigDecimal calculateWeeklyRevenue(@Param("date") LocalDate date);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE MONTH(t.transactionDate) = MONTH(:date) AND YEAR(t.transactionDate) = YEAR(:date)")
    BigDecimal calculateMonthlyRevenue(@Param("date") LocalDate date);

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate >= :date")
    List<Transaction> findAllByTransactionDateAfter(@Param("date") LocalDateTime date);




}