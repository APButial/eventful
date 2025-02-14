package com.btp.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class Event {
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
}
