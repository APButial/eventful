package com.btp.appfx.model;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
public class User {
    private String username;
    private String password;
    private List<BaseEvent> events;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
