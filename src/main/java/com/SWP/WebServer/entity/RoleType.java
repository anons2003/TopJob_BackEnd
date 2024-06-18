package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table()
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleTypeId;

    private String roleTypeName;

    @OneToMany(targetEntity = User.class,mappedBy = "roleType",cascade = CascadeType.ALL)
    private List<User> userList;

}
