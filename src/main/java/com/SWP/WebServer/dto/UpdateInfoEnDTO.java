package com.SWP.WebServer.dto;

public class UpdateInfoEnDTO {
    private String phone;
    private String location;
    private String en_position;
    private String taxcode;

    public UpdateInfoEnDTO(String phone, String location, String en_position, String taxcode) {
        this.phone = phone;
        this.location = location;
        this.en_position = en_position;
        this.taxcode = taxcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
