package com.SWP.WebServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class JobSeeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jid;

    private String first_name;
    private String last_name;
    private String occupation;
    private String intro;
    private String resume_url;
    private String web_url;
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
    //
    public JobSeeker(User user) {
        this.user = user;
        this.first_name = "";
        this.last_name = "";
        this.occupation = "";
        this.intro = "";
        this.resume_url = "";
        this.web_url = "";
        this.gender = 0;
        this.dob = "";
    }

}
