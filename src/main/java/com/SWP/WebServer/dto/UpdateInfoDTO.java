package com.SWP.WebServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInfoDTO {
    private int id;
    private String city;
    private String state;
    private String first_name;
    private String last_name;
    private String user_name;
    private String occupation;
    private String intro;
    private String email;
    private String resume_url;
}
