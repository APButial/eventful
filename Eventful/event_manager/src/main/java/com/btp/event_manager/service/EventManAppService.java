package com.btp.event_manager.service;

import com.btp.appfx.enums.SaveStatus;
import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.model.User;
import com.btp.appfx.service.AppService;
import com.btp.event_manager.model.EventManState;
import com.btp.logs.service.LogService;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventManAppService implements AppService, LogService {
    private EventManState eventManState;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm:ss");
    final String filename = "Eventful/dat/logs.txt";

    public EventManAppService(EventManState eventManState) { this.eventManState = eventManState;}

    @Override
    public void save(String filename) {

    }

    @Override
    public void exit() {

    }

    @Override
    public void close() {

    }

    @Override
    public void login(User user) {
        eventManState.setCurrUser(user);
        _loggedIn();
    }

    @Override
    public void logout() {

    }

    @Override
    public void createAccount() {

    }

    @Override
    public void deleteAccount() {

    }

    @Override
    public void createEvent(BaseEvent baseEvent) {
            baseEvent.setLastAccessed(getSysDateTime());
            eventManState.getCurrUser().getEvents().add(baseEvent);
            setSelectedEvent(baseEvent);
            setSaveStatus(SaveStatus.SAVED);
    }

    @Override
    public void updateEvent(BaseEvent baseEvent) {

    }

    @Override
    public void removeEvent(BaseEvent baseEvent) {

    }

    @Override
    public BaseEvent getSelectedEvent() {
        return eventManState.getCurrSelectedEvent();
    }

    @Override
    public void setSelectedEvent(BaseEvent baseEvent) {
        eventManState.setCurrSelectedEvent(baseEvent);
    }

    @Override
    public String getEventName() {
        return eventManState.getCurrSelectedEvent().getEventName();
    }

    @Override
    public void setEventName(String eventName) {
        eventManState.getCurrSelectedEvent().setEventName(eventName);
    }

    @Override
    public LocalDate getStartDate() {
        return eventManState.getCurrSelectedEvent().getStartDate();
    }

    @Override
    public void setStartDate(LocalDate date) {
        eventManState.getCurrSelectedEvent().setStartDate(date);
    }

    @Override
    public LocalDate getEndDate() {
        return eventManState.getCurrSelectedEvent().getEndDate();
    }

    @Override
    public void setEndDate(LocalDate date) {
        eventManState.getCurrSelectedEvent().setEndDate(date);
    }

    @Override
    public LocalTime getStartTime() {
        return eventManState.getCurrSelectedEvent().getStartTime();
    }

    @Override
    public void setStartTime(LocalTime time) {
        eventManState.getCurrSelectedEvent().setStartTime(time);
    }

    @Override
    public LocalTime getEndTime() {
        return eventManState.getCurrSelectedEvent().getEndTime();
    }

    @Override
    public void setEndTime(LocalTime time) {
        eventManState.getCurrSelectedEvent().setEndTime(time);
    }

    @Override
    public String getDescription() {
        return eventManState.getCurrSelectedEvent().getDescription();
    }

    @Override
    public void setDescription(String description) {
        eventManState.getCurrSelectedEvent().setDescription(description);
    }

    @Override
    public List<String> getGuests() {
        return eventManState.getCurrSelectedEvent().getGuests();
    }

    @Override
    public void setGuests(List<String> guests) {
        eventManState.getCurrSelectedEvent().setGuests(guests);
    }

    @Override
    public void addGuest(String guest) {
        eventManState.getCurrSelectedEvent().getGuests().add(guest);
    }

    @Override
    public void removeGuest(String guest) {
        eventManState.getCurrSelectedEvent().getGuests().remove(guest);
    }

    @Override
    public void inviteGuest(String guest) {

    }

    @Override
    public LocalDateTime getLastAccessed() {
        return eventManState.getCurrSelectedEvent().getLastAccessed();
    }

    @Override
    public void setLastAccessed(LocalDateTime dateTime) {
        eventManState.getCurrSelectedEvent().setLastAccessed(dateTime);
    }

    @Override
    public Application getPrevApplication() {
        return eventManState.getPrevApplication();
    }

    @Override
    public void setPrevApplication(Application application) {
        eventManState.setPrevApplication(application);
    }

    @Override
    public boolean isLoggedIn() {
        return eventManState.isLoggedIn();
    }

    @Override
    public void setLogIn(boolean loggedIn) {
        eventManState.setLoggedIn(loggedIn);
    }

    @Override
    public LocalDateTime getSysDateTime() {
        return eventManState.getCurrDateTime();
    }

    @Override
    public void setSysDateTime(LocalDateTime localDateTime) {
        eventManState.setCurrDateTime(localDateTime);
    }

    @Override
    public User getCurrUser() {
        return eventManState.getCurrUser();
    }

    @Override
    public void setCurrUser(User user) {
        eventManState.setCurrUser(user);
    }

    @Override
    public SaveStatus getSaveStatus() {
        return eventManState.getSaveStatus();
    }

    @Override
    public void setSaveStatus(SaveStatus saveStatus) {
        eventManState.setSaveStatus(saveStatus);
    }

    @Override
    public String getEmailAdd() {
        return eventManState.getEmailAdd();
    }

    @Override
    public void setEmailAdd(String emailAdd) {
        eventManState.setEmailAdd(emailAdd);
    }

    @Override
    public String getEmailPass() {
        return eventManState.getEmailPass();
    }

    @Override
    public void setEmailPass(String emailPass) {
        eventManState.setEmailPass(emailPass);
    }

    // methods prefixed with '_' are for logging purposes

    @Override
    public void _updateLogs(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("(" + getCurrUser().getUsername() + ") [" + getSysDateTime().format(formatter) + "]: " + text + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String _loadLogs() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = reader.readLine() ) != null) {
                content.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    @Override
    public void _loggedIn() {
        _updateLogs("Successfully logged in into this user account.");
    }

    @Override
    public void _eventsLoaded() {
        _updateLogs("Loaded a total of " + eventManState.getCurrEvents().size() + " user created events.");
    }

    @Override
    public void _accessedEvent() {
        _updateLogs("Accessed event details of " + eventManState.getCurrSelectedEvent().getEventName() + ".");
    }

    @Override
    public void _createdEvent() {
        _updateLogs("Created an event named, " + eventManState.getCurrSelectedEvent().getEventName() + ", scheduled from " + 
                    eventManState.getCurrSelectedEvent().getStartDate() + " to " + eventManState.getCurrSelectedEvent().getEndDate());
    }

    @Override
    public void _savedEvent() {
    }

    @Override
    public void _startDateUpdated() {

    }

    @Override
    public void _endDateUpdated() {

    }

    @Override
    public void _startTimeUpdated() {

    }

    @Override
    public void _endTimeUpdated() {

    }

    @Override
    public void _descriptionUpdated() {

    }

    @Override
    public void _guestsUpdated() {

    }

    @Override
    public void _budgetTrackerUpdated() {

    }

    @Override
    public void _printedTimeline() {

    }
}
