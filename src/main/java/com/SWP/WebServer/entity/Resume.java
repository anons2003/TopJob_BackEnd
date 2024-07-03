package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "resume")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "j_uid")
    private int uId;
    private String timeStamp;

    @Column(name = "userProfilePic", columnDefinition = "LONGTEXT")
    private String userProfilePic;
    private String templateName;

    // FormData fields
    private String fullname;
    private String professionalTitle;
    private String personalDescription;
    private String refererName;
    private String refererRole;
    private String mobile;
    private String email;
    private String website;
    private String address;

    @Column(name = "imageURL", columnDefinition = "LONGTEXT")
    private String imageURL;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Education> education;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Experience> experiences;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Skill> skills;

    @Override
    public String toString() {
        return "Resume{" +
                "id=" + id +
                ", uId=" + uId +
                ", timeStamp='" + timeStamp + '\'' +
                ", userProfilePic='" + userProfilePic + '\'' +
                ", templateName='" + templateName + '\'' +
                ", fullname='" + fullname + '\'' +
                ", professionalTitle='" + professionalTitle + '\'' +
                ", personalDescription='" + personalDescription + '\'' +
                ", refererName='" + refererName + '\'' +
                ", refererRole='" + refererRole + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", address='" + address + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", education=" + education.stream().map(Education::getId).toList() +
                ", experiences=" + experiences.stream().map(Experience::getId).toList() +
                ", skills=" + skills.stream().map(Skill::getId).toList() +
                '}';
    }
}
