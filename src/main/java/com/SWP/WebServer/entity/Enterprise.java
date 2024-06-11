package com.SWP.WebServer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eid;
    private String sid;
    private String avatar_url;
    private String enterprise_name;
    private String email;
    private String password;
    private String phone;
    private String location;
    private String en_position;
    private String taxcode;
    private int is_verify_email;
    private Date created_at;
    private Date updated_at;
    private String role;

    public Enterprise(String enterprise_name, String email, String password, String avatar_url, String sid, int is_verify_email) {
        this.enterprise_name = enterprise_name;
        this.email = email;
        this.password = password;
        this.avatar_url = avatar_url;
        this.sid = sid;
        this.is_verify_email = is_verify_email;
        this.role = "enterprise";
        this.created_at = new Date();
        this.updated_at = new Date();
        this.phone= null;
    }

}
