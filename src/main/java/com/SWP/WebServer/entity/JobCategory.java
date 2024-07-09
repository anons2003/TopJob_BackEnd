package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_category")
public class JobCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_category_id")
    private int jobCategoryId;

    @Column(name = "job_category_name")
    private String jobCategoryName;

    @JsonIgnoreProperties("jobCategoryEntity")
    @OneToMany(mappedBy = "jobCategoryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Job> jobList;
}
