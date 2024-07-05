package com.SWP.WebServer.dto;

import com.SWP.WebServer.entity.Education;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationDTO {
    private int id;
    private String major;
    private String university;

    public EducationDTO(Education education) {
    }
}
