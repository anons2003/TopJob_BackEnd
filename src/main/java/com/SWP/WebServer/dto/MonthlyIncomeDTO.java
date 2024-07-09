package com.SWP.WebServer.dto;

import java.math.BigDecimal;

public class MonthlyIncomeDTO {

    private int month;
    private BigDecimal totalIncome;

    public MonthlyIncomeDTO(int month, BigDecimal totalIncome) {
        this.month = month;
        this.totalIncome = totalIncome;
    }

    // Getters and setters
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }
}
