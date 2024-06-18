package com.SWP.WebServer.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppliedCVDto {
    private String full_name;
    private String email;
    private String phone;
    private String job;
    private String jobType;
    private String description;
}
