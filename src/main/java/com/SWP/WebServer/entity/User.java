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

    @Column(length = 16)
    private String user_name;
    private String email;
    private String password;
    private byte isActive;
    private int is_verify_email;
    private Date created_at;
    private Date updated_at;
    //
    private double account_balance;

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
            String gid,
            int is_verify_email) {
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.gid = gid;
        this.is_verify_email = is_verify_email;
        this.created_at = new Date();
        this.updated_at = new Date();
        this.isActive=0;
        this.account_balance=0;
    }

}
