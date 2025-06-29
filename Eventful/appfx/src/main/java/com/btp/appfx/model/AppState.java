package com.btp.appfx.model;

import com.btp.appfx.enums.SaveStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AppState {
    protected List<BaseEvent> currEvents;
    protected BaseEvent currSelectedEvent;
    protected User currUser;
    protected LocalDateTime currDateTime;
    protected boolean isLoggedIn;
    protected SaveStatus saveStatus;
    protected String emailAdd;
    protected String emailPass;

    // buffers
    protected LocalDate startDateBuffer;
    protected LocalDate endDateBuffer;
    protected LocalTime startTimeBuffer;
    protected LocalTime endTimeBuffer;
    protected String descriptionBuffer;

    public LocalDate getStartDateBuffer() {
        return startDateBuffer;
    }

    public void setStartDateBuffer(LocalDate startDateBuffer) {
        this.startDateBuffer = startDateBuffer;
    }

    public LocalDate getEndDateBuffer() {
        return endDateBuffer;
    }

    public void setEndDateBuffer(LocalDate endDateBuffer) {
        this.endDateBuffer = endDateBuffer;
    }

    public LocalTime getStartTimeBuffer() {
        return startTimeBuffer;
    }

    public void setStartTimeBuffer(LocalTime startTimeBuffer) {
        this.startTimeBuffer = startTimeBuffer;
    }

    public LocalTime getEndTimeBuffer() {
        return endTimeBuffer;
    }

    public void setEndTimeBuffer(LocalTime endTimeBuffer) {
        this.endTimeBuffer = endTimeBuffer;
    }

    public String getDescriptionBuffer() {
        return descriptionBuffer;
    }

    public void setDescriptionBuffer(String descriptionBuffer) {
        this.descriptionBuffer = descriptionBuffer;
    }

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

    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public String getEmailPass() {
        return emailPass;
    }

    public void setEmailPass(String emailPass) {
        this.emailPass = emailPass;
    }
}