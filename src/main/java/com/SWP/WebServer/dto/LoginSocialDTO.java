package com.SWP.WebServer.dto;


public class LoginSocialDTO {

    private String name;
    private String picture;
    private String sid;

    private String email;


    public LoginSocialDTO(String name, String picture, String sid, String email) {
        this.name = name;
        this.picture = picture;
        this.sid = sid;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getS_id() {
        return this.sid;
    }

    public void setS_id(String sid) {
        this.sid = sid;
    }

}
