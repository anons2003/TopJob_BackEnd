package com.SWP.WebServer.dto;

public class SignupEDTO {
    private String enterprise_name;
    private String email;
    private String password;
    private String location;
    private String en_position;
    private String taxcode;

    public SignupEDTO(String enterprise_name, String email, String password, String location, String en_position, String taxcode) {
        this.enterprise_name = enterprise_name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.en_position = en_position;
        this.taxcode = taxcode;
    }

    public String getEnterprise_name() {
        return enterprise_name;
    }

    public void setEnterprise_name(String enterprise_name) {
        this.enterprise_name = enterprise_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEn_position() {
        return en_position;
    }

    public void setEn_position(String en_position) {
        this.en_position = en_position;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }
}
