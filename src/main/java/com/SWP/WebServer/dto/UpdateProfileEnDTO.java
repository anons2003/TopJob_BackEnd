package com.SWP.WebServer.dto;

public class UpdateProfileEnDTO {
    private String penterprise_name;

    public UpdateProfileEnDTO() {
    }

    public UpdateProfileEnDTO(String penterprise_name) {
        this.penterprise_name = penterprise_name;
    }

    public String getPenterprise_name() {
        return penterprise_name;
    }

    public void setPenterprise_name(String penterprise_name) {
        this.penterprise_name = penterprise_name;
    }
}
