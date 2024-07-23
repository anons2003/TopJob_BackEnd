package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_type")
public class JobType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_type_id")
    private int jobTypeId;
    @Column(name = "job_type_name")
    private String jobTypeName;
    @JsonIgnore
    @OneToMany(mappedBy = "jobTypeEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Job> jobList;


}