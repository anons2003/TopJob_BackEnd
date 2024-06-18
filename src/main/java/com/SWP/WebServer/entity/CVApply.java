package com.SWP.WebServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CVApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cvId;
    private String full_name;
    private String email;
    private String phone;
    private String job;
    private String jobType;
    private String description;
    private byte isApllied;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "userId",
            referencedColumnName = "uid"
    )
    private User user;

    @ManyToOne(
            cascade = CascadeType.ALL
    )

    @JoinColumn(
            name ="eid",
            referencedColumnName = "eid"
    )
    private Enterprise enterprise;
}
