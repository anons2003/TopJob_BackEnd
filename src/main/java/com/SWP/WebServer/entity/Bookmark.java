package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

    @Id
    @Column(name = "bookmark_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(
            name = "job_id",
            referencedColumnName ="id"
    )
    @JsonIgnoreProperties("bookmarks")
    private Job jobId;

    @ManyToOne()
    @JoinColumn(
            name = "job_seeker_id",
            referencedColumnName = "jid"
    )
    @JsonIgnoreProperties("bookmarks")
    private JobSeeker jobSeekers;

    private byte isBookmarked;

}
