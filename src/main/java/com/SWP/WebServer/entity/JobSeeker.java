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

    //
    private String avatar_url;
    @Column(length = 45)
    private String phone;
    private String city;
    private String state;
    private String web_url;
    private String resume_url;
    //
    private String first_name;
    private String last_name;
    private String occupation;
    @Column(columnDefinition = "TEXT")
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
    //
    public JobSeeker(User user) {
        this.user = user;
        this.avatar_url = "http://res.cloudinary.com/dswewjrly/image/upload/v1715831315/wmndhsmpxuihewekekzy.jpg";
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
