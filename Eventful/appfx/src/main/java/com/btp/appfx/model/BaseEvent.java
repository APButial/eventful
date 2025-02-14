package com.btp.appfx.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class BaseEvent {
    // mandatory event details
    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;

    // optional event details
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;

    private List<String> guests;

    // metadata
    private String creator;
    private LocalDateTime lastAccessed;

    BaseEvent(String eventName, LocalDate startDate, LocalDate endDate) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
