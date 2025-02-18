package com.btp.appfx.service;

import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.model.User;
import javafx.application.Application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface AppService {
    // Basic
    void save(String filename);
    void exit();
    void close();

    // Login
    void login();
    void logout();
    void createAccount();
    void deleteAccount();

    // BaseEvent
    void createEvent();
    void updateEvent(BaseEvent baseEvent);
    void removeEvent(BaseEvent baseEvent);
    BaseEvent getSelectedEvent();
    void setSelectedEvent(BaseEvent baseEvent);
    String getEventName();
    void setEventName(String eventName);
    LocalDate getStartDate();
    void setEventStartDate(LocalDate date);
    LocalDate getEndDate();
    void setEndDate(LocalDate date);
    LocalTime getStartTime();
    void setStartTime(LocalTime time);
    LocalTime getEndTime();
    void setEndTime(LocalTime time);
    String getDescription();
    void setDescription(String description);
    List<String> getGuests();
    void addGuest(String guest);
    void removeGuest(String guest);
    void inviteGuest(String guest);
    LocalDateTime getLastAccessed();
    void setLastAccessed(LocalDateTime dateTime);

    // others
    Application getPrevApplication();
    void setPrevApplication(Application application);
    boolean isLoggedIn();
    void setLogIn(boolean loggedIn);
    LocalDateTime getSysDateTime();
    void setSysDateTime(LocalDateTime localDateTime);
    User getCurrUser();
    void setCurrUser(User user);
}
