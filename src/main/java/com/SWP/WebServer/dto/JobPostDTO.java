package com.SWP.WebServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostDTO {
    private int jobCategory;
    private int jobTypeId;
    private String description;
    private String title;
    private String skills;
    private String qualifications;
    private String experience;
    private String industry;
    private BigDecimal maxSalary;
    private BigDecimal minSalary;
    private String salaryType;
    private String address;
    private String country;
    private String state;
}
