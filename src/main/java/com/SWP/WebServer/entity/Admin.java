package com.SWP.WebServer.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private int uid;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "is_active")
    private Boolean isActive;

    //
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "userId",
            referencedColumnName = "uid"
    )
    private User user;

    // getters and setters
}