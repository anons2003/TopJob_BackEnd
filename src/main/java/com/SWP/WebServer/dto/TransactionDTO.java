package com.SWP.WebServer.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {

    private int transactionId;
    private int userId;
    private int packageId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;

    private BigDecimal amount;

    // Constructors, getters, and setters
    // Omitted for brevity

    public TransactionDTO() {
    }

    public TransactionDTO(int transactionId, int userId, int packageId, LocalDateTime transactionDate, BigDecimal amount) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.packageId = packageId;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }
}