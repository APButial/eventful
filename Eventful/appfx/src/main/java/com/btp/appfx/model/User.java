package com.btp.appfx.model;

import java.util.List;

public class User {
    private String username;
    private String password;
    private List<BaseEvent> events;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
