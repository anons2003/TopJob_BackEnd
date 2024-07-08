package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name = "job_type")
    private int jobType;

    @Column(name = "job_category")
    private int jobCategory;

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
    
    // Getters and setters
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name="posted_eid",
            referencedColumnName = "eid"
    )
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

    //Thai Son

    //Old Hibernate ManyToMany Relationship
//    @JsonIgnoreProperties("bookmarkedJobs")
//    @ManyToMany
//    @JoinTable(
//            name = "job_seeker_job_map",
//            joinColumns = @JoinColumn(
//                    name = "job_id",
//                    referencedColumnName = "id"
//            )
//            ,
//            inverseJoinColumns =@JoinColumn(
//                    name = "job_seeker_id",
//                    referencedColumnName = "jid"
//            )
//    )
//    private List<JobSeeker> jobSeekers;

    //New One
    @JsonIgnoreProperties("jobId")
    @OneToMany(mappedBy = "jobId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks;

}
