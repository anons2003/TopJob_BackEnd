package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.Transaction;
import com.SWP.WebServer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long transactionId) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
        return optionalTransaction.orElse(null);
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long transactionId, Transaction updatedTransaction) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
        if (optionalTransaction.isPresent()) {
            updatedTransaction.setTransactionId(transactionId);
            return transactionRepository.save(updatedTransaction);
        }
        return null;
    }

    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }
    // Phương thức lấy danh sách các giao dịch trong khoảng thời gian từ startDateTime đến endDateTime
    public List<Transaction> getAllTransactionsWithinWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        LocalDateTime now = LocalDateTime.now();
        return transactionRepository.findByTransactionDateBetween(oneWeekAgo, now);
    }
    // danh thu 1 ngày
    public BigDecimal calculateDailyRevenue(LocalDate date) {
        LocalDate startDateTime = date.atStartOfDay().toLocalDate();
        LocalDate endDateTime = startDateTime.plusDays(1);

        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(
                startDateTime.atStartOfDay(),
                endDateTime.atStartOfDay().minusNanos(1)
        );

        BigDecimal totalRevenue = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalRevenue;
    }

    public BigDecimal calculateWeeklyRevenue(LocalDate date) {
        // Lấy ngày bắt đầu của tuần trước
        LocalDate startDateTime = date.minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
        LocalDate endDateTime = startDateTime.plusWeeks(1);

        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(
                startDateTime.atStartOfDay(),
                endDateTime.atStartOfDay().minusNanos(1)
        );

        BigDecimal totalRevenue = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalRevenue;
    }

    public BigDecimal calculateMonthlyRevenue(LocalDate date) {
        // Lấy ngày bắt đầu của tháng trước
        LocalDate startDateTime = date.minusMonths(1).withDayOfMonth(1);
        LocalDate endDateTime = startDateTime.plusMonths(1);

        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(
                startDateTime.atStartOfDay(),
                endDateTime.atStartOfDay().minusNanos(1)
        );

        BigDecimal totalRevenue = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalRevenue;
    }




}
