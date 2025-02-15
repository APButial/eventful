package com.btp.appfx.model;

import javafx.scene.Scene;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppState {
    protected List<BaseEvent> currEvents;
    protected BaseEvent currSelectedEvent;
    protected Scene currentScene;
    protected Scene prevScene;
    protected String currUser;
    protected LocalDateTime currDateTime;
    protected boolean isLoggedIn;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public LocalDateTime getCurrDateTime() {
        return currDateTime;
    }

    public void setCurrDateTime(LocalDateTime localDateTime) {
        currDateTime = localDateTime;
    }

    public String getCurrUser() {
        return getCurrUser();
    }

    public void setCurrUser(String currUser) {
        this.currUser = currUser;
    }
}