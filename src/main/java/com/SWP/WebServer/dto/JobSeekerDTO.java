package com.SWP.WebServer.dto;

import lombok.Data;

@Data
public class JobSeekerDTO {
    private String userName;
    private String password;
    private String email;
    private int userRoleId;
}
