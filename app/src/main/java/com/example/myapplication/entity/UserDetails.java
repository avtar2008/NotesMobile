package com.example.myapplication.entity;

import lombok.Data;

@Data
public class UserDetails {
    private String username;
    private String password;

    public UserDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
