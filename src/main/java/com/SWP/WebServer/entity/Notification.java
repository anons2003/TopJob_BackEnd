package com.SWP.WebServer.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class
Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private boolean isRead;

    @ManyToOne
    @JsonIgnoreProperties("notifications")
    @JoinColumn(name = "job_seeker_id",referencedColumnName = "jid")
    private JobSeeker jobSeeker;

    @ManyToOne
    @JsonIgnoreProperties({"cvApplies,bookmarks"})
    @JoinColumn(name = "job_id",referencedColumnName = "id")
    private Job job;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    private LocalDateTime deletionTime;

}
