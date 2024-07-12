package com.SWP.WebServer.dto;

import com.SWP.WebServer.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDTO {
    private int id;
    private String title;
    private String percentage;


    public SkillDTO(Skill skill) {

    }
}
