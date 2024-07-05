package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "education")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String major;
    private String university;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", referencedColumnName = "id")
    @JsonBackReference
    private Resume resume;
    // Getters and setters
    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }
    @Override
    public String toString() {
        return "Education{" +
                "id=" + id +
                ", major='" + major + '\'' +
                ", university='" + university + '\'' +
                ", resumeId=" + (resume != null ? resume.getId() : null) +
                '}';
    }
}
