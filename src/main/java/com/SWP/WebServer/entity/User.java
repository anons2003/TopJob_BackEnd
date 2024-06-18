package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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
    private int uid;
    private String gid;
    private String avatar_url;
    @Column(length = 16)
    private String user_name;
    private String email;
    private String password;
    @Column(length = 45)
    private String phone;
    private byte isActive;
    private int is_verify_email;
    private Date created_at;
    private Date updated_at;
    //
    private double account_balance;
    private String city;
    private String state;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String web_url;
    private String resume_url;
    //
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_role_id",
            referencedColumnName = "roleTypeId"
    )
    @JsonIgnoreProperties("userList") // Ignore userList property of RoleType during serialization
    private RoleType roleType;



    public User(
            String user_name,
            String email,
            String password,
            String avatar_url,
            String gId,
            int is_verify_email) {
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.avatar_url = avatar_url;
        this.gid = gid;
        this.is_verify_email = is_verify_email;
        this.created_at = new Date();
        this.updated_at = new Date();
        this.isActive=0;
        this.phone = null;
        this.city = null;
        this.state = null;
        this.account_balance=0;
        this.web_url = "";
        this.resume_url="";
    }

}
