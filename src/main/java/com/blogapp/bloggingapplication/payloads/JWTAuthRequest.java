package com.blogapp.bloggingapplication.payloads;

import lombok.Data;

@Data
public class JWTAuthRequest {
private String username;
private String passward;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return passward;
    }

    public void setPassword(String password) {
        this.passward = password;
    }
}
