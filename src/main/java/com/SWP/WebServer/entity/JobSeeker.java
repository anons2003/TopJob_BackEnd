package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class
JobSeeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jid;

    //
    private String avatar_url;
    @Column(length = 45)
    private String phone;
    private String city;
    private String state;
    private String web_url;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String resume_url;
    //
    private String first_name;
    private String last_name;
    private String occupation;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String intro;
    private byte gender;
    private String dob;
    //
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "userId",
            referencedColumnName = "uid"
    )
    private User user;

    //Old bookmark
//    @JsonIgnoreProperties("jobSeekers")
//    @ManyToMany(mappedBy = "jobSeekers")
//    private List<Job> bookmarkedJobs;

    //

    //New bookmark
    @JsonIgnoreProperties("jobSeekers")
    @OneToMany(mappedBy = "jobSeekers", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks;
    //

    public JobSeeker(User user) {
        this.user = user;
        this.avatar_url = "https://res.cloudinary.com/dz9kynjwb/image/upload/v1717770585/OIP_bsmlku.jpg";
        this.first_name = "";
        this.last_name = "";
        this.occupation = "";
        this.intro = "";
        this.resume_url = "";
        this.web_url = "";
        this.gender = 0;
        this.dob = "";
        this.phone = "";
        this.city = "";
        this.state = "";

    }

}
