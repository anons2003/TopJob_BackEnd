package com.SWP.WebServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInfoEnDTO {
    private int id;
    private String enterprise_name;
    private String founded;
    private String founder;
    private String state;
    private String city;
    private String phone;
    private String headquarter;
    private String companyStory;
    private String employees;
}
