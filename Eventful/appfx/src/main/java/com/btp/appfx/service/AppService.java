package com.btp.appfx.service;

import com.btp.appfx.model.BaseEvent;

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
    BaseEvent getEvent();
    void setEvent(BaseEvent baseEvent);
    String getEventName();
    void setEventName(String eventName);
    LocalDate getStartDate();
    void setStartDate(LocalDate date);
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
    String getCreator();
    void setCreator(String user);
    LocalDateTime getLastAccessed();
    void setLastAccessed(LocalDateTime dateTime);
}
