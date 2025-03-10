package com.btp.event_manager.service;

import com.btp.appfx.enums.EventFormEvents;
import com.btp.appfx.enums.SaveStatus;
import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.model.User;
import com.btp.appfx.service.AppService;
import com.btp.event_manager.model.EventManState;
import com.btp.logs.service.LogService;
import javafx.application.Application;

import javax.mail.internet.AddressException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventManAppService implements AppService, LogService {
    private EventManState eventManState;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm:ss");
    final String filepath = "Eventful/dat/";

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
        LoadUserEvents.load(this);
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
            _createdEvent();
            setSaveStatus(SaveStatus.SAVED);
    }

    @Override
    public void updateEvent(EventFormEvents update) {

    }

    @Override
    public void updateEvent(EventFormEvents update, String input) {
        switch (update) {
            case DESC:
                setDescription(input.strip());
                _descriptionUpdated();
                setSaveStatus(SaveStatus.UNSAVED);
                break;
            case UPDATE_CHANGES:
                if (!RegexService.validate(input)){
                    return;
                }

                setGuests(List.of(input.split(";")));
                WriteEventsService.overwrite(this);
                _savedEvent();
                LoadUserEvents.load(this);
                setSaveStatus(SaveStatus.SAVED);
                break;
        }
    }

    @Override
    public void updateEvent(EventFormEvents update, LocalDate input) {
        switch (update) {
            case START_DATE:
                setStartDate(input);
                _startDateUpdated();
                break;
            case END_DATE:
                setEndDate(input);
                _endDateUpdated();
                break;
        }
        setSaveStatus(SaveStatus.UNSAVED);
    }

    @Override
    public void updateEvent(EventFormEvents update, LocalTime input) {
        switch (update) {
            case START_TIME:
                setStartTime(input);
                _startTimeUpdated();
                break;
            case END_TIME:
                setEndTime(input);
                _endTimeUpdated();
                break;
        }
        setSaveStatus(SaveStatus.UNSAVED);
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
        _accessedEvent();
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
    public void inviteGuests(String guests) throws AddressException {
        if((getEmailAdd() == null || getEmailPass() == null) && MailService.authenticate(this)) {
            if (MainFrameAlerts.sendEmailConfirmation()) {
                if(MailService.validMailArea(guests, this)) {
                    MailService.sendMail(this);
                    _guestsInvited();
                }
            }
        }
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath + getCurrUser().getUsername() + "/logs.txt", true))) {
            writer.write("(" + getCurrUser().getUsername() + ") [" + getSysDateTime().format(formatter) + "]: " + text);
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String _loadLogs() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath + getCurrUser().getUsername() + "/logs.txt"))) {
            String line;
            List<String> entries = new ArrayList<>();
            while((line = reader.readLine() ) != null) {
                entries.add(line);
            }

            Collections.reverse(entries);
            for (String entry: entries) {
                content.append(entry).append("\n");
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
        _updateLogs("Loaded a total of " + eventManState.getCurrUser().getEvents().size() + " user created events.");
    }

    @Override
    public void _accessedEvent() {
        _updateLogs("Accessed event details of " + getSelectedEvent().getEventName() + ".");
    }

    @Override
    public void _createdEvent() {
        _updateLogs("Created an event named, " + getEventName() + ", scheduled from " +
                    getSelectedEvent().getStartDate() + " to " + getSelectedEvent().getEndDate() + ".");
    }

    @Override
    public void _savedEvent() {
        _updateLogs("Overwrote event details in " + getSelectedEvent().getEventName());
    }

    @Override
    public void _startDateUpdated() {
        _updateLogs("Changed start date of " + getSelectedEvent().getEventName() + " to " + getSelectedEvent().getStartDate() + ".");
    }

    @Override
    public void _endDateUpdated() {
        _updateLogs("Changed end date of " + getSelectedEvent().getEventName() + " to " + getSelectedEvent().getEndDate() + ".");
    }

    @Override
    public void _startTimeUpdated() {
        _updateLogs("Changed start time of " + getSelectedEvent().getEventName() + " to " + getSelectedEvent().getStartTime() + ".");
    }

    @Override
    public void _endTimeUpdated() {
        _updateLogs("Changed end time of " + getSelectedEvent().getEventName() + " to " + getSelectedEvent().getEndTime() + ".");
    }

    @Override
    public void _descriptionUpdated() {
        _updateLogs("Changed description of " + getSelectedEvent().getEventName() + " to " + getSelectedEvent().getDescription() + ".");
    }

    @Override
    public void _guestsInvited() {
        _updateLogs("Sent an invitation email to the following addresses: " + String.join(";", getGuests()) + ".");
    }

    @Override
    public void _budgetTrackerUpdated() {

    }

    @Override
    public void _printedTimeline() {

    }
}
