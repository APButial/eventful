package com.btp.appfx.model;


import com.btp.appfx.enums.EventStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BaseEvent {
    // mandatory event details
    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;

    // optional event details
    private String eventID = null; // for google calendar
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private EventStatus status;

    private List<String> guests;

    // metadata
    private String creator;
    private LocalDateTime lastAccessed;


    public BaseEvent(String eventName, LocalDate startDate, LocalDate endDate) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute());
        this.endTime = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute());
        this.endTime = this.endTime.plusHours(1);
        this.description = "";
        this.status = EventStatus.DRAFT;
        guests = new ArrayList<>();
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGuests() {
        return guests;
    }

    public void setGuests(List<String> guests) {
        this.guests = guests;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
