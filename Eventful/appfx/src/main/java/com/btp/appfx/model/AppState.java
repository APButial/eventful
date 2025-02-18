package com.btp.appfx.model;

import com.btp.appfx.enums.SaveStatus;
import javafx.scene.Scene;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppState {
    protected List<BaseEvent> currEvents;
    protected BaseEvent currSelectedEvent;
    protected User currUser;
    protected LocalDateTime currDateTime;
    protected boolean isLoggedIn;
    protected SaveStatus saveStatus;

    public SaveStatus getSaveStatus() {
        return saveStatus;
    }

    public void setSaveStatus(SaveStatus saveStatus) {
        this.saveStatus = saveStatus;
    }

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

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public List<BaseEvent> getCurrEvents() {
        return currEvents;
    }

    public void setCurrEvents(List<BaseEvent> currEvents) {
        this.currEvents = currEvents;
    }

    public BaseEvent getCurrSelectedEvent() {
        return currSelectedEvent;
    }

    public void setCurrSelectedEvent(BaseEvent currSelectedEvent) {
        this.currSelectedEvent = currSelectedEvent;
    }
}