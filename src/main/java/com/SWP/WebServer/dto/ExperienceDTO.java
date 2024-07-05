package com.SWP.WebServer.dto;

import com.SWP.WebServer.entity.Experience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDTO {
    private int id;
    private String year;
    private String title;
    private String companyAndLocation;
    private String description;

    public ExperienceDTO(Experience experience) {
    }
}
