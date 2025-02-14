package com.btp.event_manager.service;

import com.btp.appfx.model.BaseEvent;
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
    public BaseEvent getEvent() {
        return null;
    }

    @Override
    public void setEvent(BaseEvent baseEvent) {

    }

    @Override
    public String getEventName() {
        return "";
    }

    @Override
    public void setEventName(String eventName) {

    }

    @Override
    public LocalDate getStartDate() {
        return null;
    }

    @Override
    public void setEventStartDate(LocalDate date) {

    }

    @Override
    public LocalDate getEndDate() {
        return null;
    }

    @Override
    public void setEndDate(LocalDate date) {

    }

    @Override
    public LocalTime getStartTime() {
        return null;
    }

    @Override
    public void setStartTime(LocalTime time) {

    }

    @Override
    public LocalTime getEndTime() {
        return null;
    }

    @Override
    public void setEndTime(LocalTime time) {

    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public List<String> getGuests() {
        return List.of();
    }

    @Override
    public void addGuest(String guest) {

    }

    @Override
    public void removeGuest(String guest) {

    }

    @Override
    public void inviteGuest(String guest) {

    }

    @Override
    public String getCreator() {
        return "";
    }

    @Override
    public void setCreator(String user) {

    }

    @Override
    public LocalDateTime getLastAccessed() {
        return null;
    }

    @Override
    public void setLastAccessed(LocalDateTime dateTime) {

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
}
