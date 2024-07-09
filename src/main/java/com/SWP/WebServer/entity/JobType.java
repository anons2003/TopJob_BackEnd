package com.SWP.WebServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jobTypeId;

    private String jobTypeName;

    @OneToMany(targetEntity = Job.class, mappedBy = "jobType", cascade = CascadeType.ALL)
    private List<Job> jobList;

}
