package com.SWP.WebServer.dto;

public class SignupDTO {
    private String user_name;
    private String email;
    private String password;

    public SignupDTO() {
    }

    public SignupDTO(String user_name, String email, String password) {
        this.user_name = user_name;
        this.email = email;
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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
}
