package com.SWP.WebServer.entity;

import jakarta.persistence.*;
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
    private String enterprise_name;
    private String founder;
    private String headquarter;
    private String founded;
    private String companyStory;
    private Date created_at;
    private Date updated_at;
    //
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "userId",
            referencedColumnName = "uid"
    )
    private User user;


    public Enterprise(User user) {
        this.user = user;
        this.created_at = new Date();
        this.updated_at = new Date();
    }

}
