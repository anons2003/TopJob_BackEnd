package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Job")
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

//    @Column(name = "job_type", insertable = false, updatable = false)
//    private Integer jobType;
//
//    @Column(name = "job_category", insertable = false, updatable = false)
//    private Integer jobCategory;

    @Column(name = "salary_type")
    private String salaryType;

    @Column(name = "min_salary")
    private BigDecimal minSalary;

    @Column(name = "max_salary")
    private BigDecimal maxSalary;

    private String skills;
    private String qualifications;
    private String experience;
    private String industry;
    private String address;
    private String country;
    private String state;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "posted_eid", referencedColumnName = "eid")
    private Enterprise enterprise;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_type", referencedColumnName = "job_type_id")
    @JsonIgnoreProperties("jobList")
    private JobType jobTypeEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_category", referencedColumnName = "job_category_id")
    // Đảm bảo khớp với thuộc tính trong JobCategory
    @JsonIgnoreProperties("jobList")
    private JobCategory jobCategoryEntity;

    @JsonIgnoreProperties("jobId")
    @OneToMany(mappedBy = "jobId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks;

    //    @JsonIgnoreProperties({"jobId","user","enterprise"})
//    @OneToMany(mappedBy = "jobId",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CVApply> cvApplies;
    @JsonIgnore
    @OneToMany(mappedBy = "jobId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CVApply> cvApplies;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
