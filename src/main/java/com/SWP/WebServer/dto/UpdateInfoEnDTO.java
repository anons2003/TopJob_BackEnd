package com.SWP.WebServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInfoEnDTO {
    private String phone;
    private String location;
    private String en_position;
    private String taxCode;

}
