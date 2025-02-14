package com.btp.event_manager.model;

import com.btp.appfx.model.BaseEvent;
import com.btp.login.service.PassHashService;

import java.util.List;

public class User {
    private String username;
    private String hashedPassword;
    private List<BaseEvent> events;

    public User(String username, String password) {
        this.username = username;
        this.hashedPassword = PassHashService.hash(password);
    }
}
