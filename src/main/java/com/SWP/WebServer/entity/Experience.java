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
@Table(name = "experience")
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String companyAndLocation;
    private String description;
    private String title;
    private String year;

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
        return "Experience{" +
                "id=" + id +
                ", companyAndLocation='" + companyAndLocation + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", resumeId=" + (resume != null ? resume.getId() : null) +
                '}';
    }
}
