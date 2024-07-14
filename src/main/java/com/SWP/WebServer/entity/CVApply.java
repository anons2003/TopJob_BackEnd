package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@JsonIgnoreProperties({ "jobId"})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CVApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cvId;
    private String full_name;
    private String email;
    private String phone;
    private String job;
    private String jobType;
    private String description;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String resume_url;
    private int isApllied;

    @ManyToOne(
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(
            name = "userId",
            referencedColumnName = "uid"
    )
    private User user;

    @ManyToOne(
            cascade = CascadeType.PERSIST
    )

    @JoinColumn(
            name ="eid",
            referencedColumnName = "eid"
    )
    private Enterprise enterprise;

    @ManyToOne(
            cascade = CascadeType.ALL
    )

    @JoinColumn(
            name = "jobId",
            referencedColumnName = "id"
    )
    @JsonIgnoreProperties({"cvApplies"})
    private Job jobId;


}
