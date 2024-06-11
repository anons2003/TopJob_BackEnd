package com.SWP.WebServer.dto;

public class ReverifyDTO {
    private String email;

    public ReverifyDTO() {
    }

    public ReverifyDTO(String mail) {
        this.email = email;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }
}
