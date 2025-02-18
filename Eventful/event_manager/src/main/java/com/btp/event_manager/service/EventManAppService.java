package com.btp.event_manager.service;

import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.model.User;
import com.btp.appfx.service.AppService;
import com.btp.event_manager.model.EventManState;
import javafx.application.Application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class EventManAppService implements AppService {
    private EventManState eventManState;

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
        return eventManState.getCurrSelectedEvent();
    }

    @Override
    public void setSelectedEvent(BaseEvent baseEvent) {
        eventManState.setCurrSelectedEvent(baseEvent);
    }

    @Override
    public String getEventName() {
        return "";
    }

    @Override
    public void setEventName(String eventName) {
        eventManState.getCurrSelectedEvent().setEventName(eventName);
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
    public LocalDateTime getLastAccessed() {
        return null;
    }

    @Override
    public void setLastAccessed(LocalDateTime dateTime) {

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

}
