package com.SWP.WebServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SignupDTO {
    private String user_name;
    private String email;
    private String password;
    private int userTypeId;
}
