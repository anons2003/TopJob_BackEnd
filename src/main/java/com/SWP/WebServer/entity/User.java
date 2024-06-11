package com.SWP.WebServer.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "[User]")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String sid;
    private String avatar_url;
    private String user_name;
    private String email;
    private String password;
    private String phone;
    private boolean Gender;
    private Date dob;
    private String role;
    private int is_verify_email;
    private Date created_at;
    private Date updated_at;
    //
    private String city;
    private String state;
    private String first_name;
    private String last_name;
    private String occupation;
    private String intro;
    private String web_url;
    private String resume_url;

    public User(
            String user_name,
            String email,
            String password,
            String avatar_url,
            String sid,
            int is_verify_email) {
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.avatar_url = avatar_url;
        this.sid = sid;
        this.is_verify_email = is_verify_email;
        this.role = "user";
        this.created_at = new Date();
        this.updated_at = new Date();
        this.isGender();
        this.phone = null;
        this.dob = null;
        this.city = null;
        this.state = null;
        this.first_name = null;
        this.last_name = null;
        this.occupation = null;
        this.intro = null;
        this.web_url = null;
        this.resume_url=null;
    }

}
