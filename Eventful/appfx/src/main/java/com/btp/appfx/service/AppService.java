package com.btp.appfx.service;

import com.btp.appfx.enums.EventFormEvents;
import com.btp.appfx.enums.SaveStatus;
import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.model.User;

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
    void login(User user);
    void logout();
    void createAccount(String user, String pass, String passConfirm);
    void deleteAccount();

    // BaseEvent
    void createEvent(BaseEvent baseEvent);
    void updateEvent(EventFormEvents update);
    void updateEvent(EventFormEvents update, String input);
    void updateEvent(EventFormEvents update, LocalDate input);
    void updateEvent(EventFormEvents update, LocalTime input);
    void removeEvent();
    BaseEvent getSelectedEvent();
    void setSelectedEvent(BaseEvent baseEvent);
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
    void setGuests(List<String> guests);
    void addGuest(String guest);
    void removeGuest(String guest);
    void inviteGuests(String guests);
    LocalDateTime getLastAccessed();
    void setLastAccessed(LocalDateTime dateTime);

    // others
    boolean isLoggedIn();
    void setLogIn(boolean loggedIn);
    LocalDateTime getSysDateTime();
    void setSysDateTime(LocalDateTime localDateTime);
    User getCurrUser();
    void setCurrUser(User user);
    SaveStatus getSaveStatus();
    void setSaveStatus(SaveStatus saveStatus);

    String getEmailAdd();
    void setEmailAdd(String emailAdd);
    String getEmailPass();
    void setEmailPass(String emailPass);
}
