package com.btp.appfx.model;

import lombok.Setter;

import java.util.List;

public class User {
    @Setter
    private String username;
    private String password;
    private List<BaseEvent> events;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
