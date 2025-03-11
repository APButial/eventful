package com.btp.event_manager.service;

import com.btp.appfx.enums.EventFormEvents;
import com.btp.appfx.enums.SaveStatus;
import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.model.User;
import com.btp.appfx.service.AppService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// Currently unused. Will need to use this when undo and redo is implemented.
public class EventManCommandService implements AppService {
    private AppService appService;

    public EventManCommandService(AppService appService) {this.appService = appService;}

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
        appService.login(user);
    }

    @Override
    public void logout() {

    }

    @Override
    public void createAccount(String user, String pass, String passConfirm) {

    }

    @Override
    public void deleteAccount() {

    }

    @Override
    public void createEvent(BaseEvent baseEvent) {
        appService.createEvent(baseEvent);
    }

    @Override
    public void updateEvent(EventFormEvents update) {

    }

    @Override
    public void updateEvent(EventFormEvents update, String input) {

    }

    @Override
    public void updateEvent(EventFormEvents update, LocalDate input) {

    }

    @Override
    public void updateEvent(EventFormEvents update, LocalTime input) {

    }

    @Override
    public void removeEvent(BaseEvent baseEvent) {

    }

    @Override
    public BaseEvent getSelectedEvent() {
        return appService.getSelectedEvent();
    }

    @Override
    public void setSelectedEvent(BaseEvent baseEvent) {
        appService.setSelectedEvent(baseEvent);
    }

    @Override
    public String getEventName() {
        return appService.getEventName();
    }

    @Override
    public void setEventName(String eventName) {
        appService.setEventName(eventName);
    }

    @Override
    public LocalDate getStartDate() {
        return appService.getStartDate();
    }

    @Override
    public void setStartDate(LocalDate date) {
        appService.setStartDate(date);
    }

    @Override
    public LocalDate getEndDate() {
        return appService.getEndDate();
    }

    @Override
    public void setEndDate(LocalDate date) {
        appService.setEndDate(date);
    }

    @Override
    public LocalTime getStartTime() {
        return appService.getStartTime();
    }

    @Override
    public void setStartTime(LocalTime time) {
        appService.setStartTime(time);
    }

    @Override
    public LocalTime getEndTime() {
        return appService.getEndTime();
    }

    @Override
    public void setEndTime(LocalTime time) {
        appService.setEndTime(time);
    }

    @Override
    public String getDescription() {
        return appService.getDescription();
    }

    @Override
    public void setDescription(String description) {
        appService.setDescription(description);
    }

    @Override
    public List<String> getGuests() {
        return appService.getGuests();
    }

    @Override
    public void setGuests(List<String> guests) {
        appService.setGuests(guests);
    }

    @Override
    public void addGuest(String guest) {
        appService.addGuest(guest);
    }

    @Override
    public void removeGuest(String guest) {
        appService.removeGuest(guest);
    }

    @Override
    public void inviteGuests(String guests) {

    }

    @Override
    public LocalDateTime getLastAccessed() {
        return appService.getLastAccessed();
    }

    @Override
    public void setLastAccessed(LocalDateTime dateTime) {
        appService.setLastAccessed(dateTime);
    }

    @Override
    public Application getPrevApplication() {
        return appService.getPrevApplication();
    }

    @Override
    public void setPrevApplication(Application application) {
        appService.setPrevApplication(application);
    }

    @Override
    public boolean isLoggedIn() {
        return appService.isLoggedIn();
    }

    @Override
    public void setLogIn(boolean loggedIn) {
        appService.setLogIn(loggedIn);
    }

    @Override
    public LocalDateTime getSysDateTime() {
        return appService.getSysDateTime();
    }

    @Override
    public void setSysDateTime(LocalDateTime localDateTime) {
        appService.setSysDateTime(localDateTime);
    }

    @Override
    public User getCurrUser() {
        return appService.getCurrUser();
    }

    @Override
    public void setCurrUser(User user) {
        appService.setCurrUser(user);
    }

    @Override
    public SaveStatus getSaveStatus() {
        return appService.getSaveStatus();
    }

    @Override
    public void setSaveStatus(SaveStatus saveStatus) {
        appService.setSaveStatus(saveStatus);
    }

    @Override
    public String getEmailAdd() {
        return appService.getEmailAdd();
    }

    @Override
    public void setEmailAdd(String emailAdd) {
        appService.setEmailAdd(emailAdd);
    }

    @Override
    public String getEmailPass() {
        return appService.getEmailPass();
    }

    @Override
    public void setEmailPass(String emailPass) {
        appService.setEmailPass(emailPass);
    }

    @Override
    public void setMainStage(Stage stage) {

    }

    @Override
    public Stage getMainStage() {
        return null;
    }
}
