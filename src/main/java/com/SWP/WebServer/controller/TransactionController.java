package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.Transaction;
import com.SWP.WebServer.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long transactionId, @RequestBody Transaction transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, transaction);
        if (updatedTransaction != null) {
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // lasted transaction within one week
    @GetMapping("/listoneweek")
    public ResponseEntity<List<Transaction>> getAllTransactionsWithinWeek() {
        List<Transaction> transactions = transactionService.getAllTransactionsWithinWeek();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }


    //  danh thu trong 1 ngày
    @GetMapping("/daily-revenue")
    public ResponseEntity<BigDecimal> getDailyRevenue() {
        BigDecimal dailyRevenue = transactionService.calculateDailyRevenue(LocalDate.now());
        return new ResponseEntity<>(dailyRevenue, HttpStatus.OK);
    }@GetMapping("/weekly-revenue")
    public ResponseEntity<BigDecimal> getWeeklyRevenue() {
        BigDecimal weeklyRevenue = transactionService.calculateWeeklyRevenue(LocalDate.now());
        return ResponseEntity.ok(weeklyRevenue != null ? weeklyRevenue.setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<BigDecimal> getMonthlyRevenue() {
        BigDecimal monthlyRevenue = transactionService.calculateMonthlyRevenue(LocalDate.now());
        return ResponseEntity.ok(monthlyRevenue != null ? monthlyRevenue.setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
    }




}
