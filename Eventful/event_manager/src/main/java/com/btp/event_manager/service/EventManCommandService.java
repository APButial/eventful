package com.btp.event_manager.service;

import com.btp.appfx.enums.SaveStatus;
import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.model.User;
import com.btp.appfx.service.AppService;
import javafx.application.Application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    public void login() {

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
    public void createEvent() {

    }

    @Override
    public void updateEvent(BaseEvent baseEvent) {

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
    public void setEventStartDate(LocalDate date) {
        appService.setEventStartDate(date);
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
    public void addGuest(String guest) {
        appService.addGuest(guest);
    }

    @Override
    public void removeGuest(String guest) {
        appService.removeGuest(guest);
    }

    @Override
    public void inviteGuest(String guest) {
        appService.inviteGuest(guest);
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

}
