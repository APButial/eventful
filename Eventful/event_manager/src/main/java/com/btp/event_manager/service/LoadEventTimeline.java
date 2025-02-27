package com.btp.event_manager.service;

import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.service.AppService;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LoadEventTimeline {
    public static void load(AppService appService, Calendar calendar) {
        try {
            for (BaseEvent event: appService.getCurrUser().getEvents()) {
                Entry<String> eventEntry = new Entry<>(event.getEventName());

                LocalDate startDate = event.getStartDate();
                LocalDate endDate = event.getEndDate();

                LocalTime startTime = LocalTime.of(0,0);
                LocalTime endTime = LocalTime.of(23, 59);
                if (event.getStartTime() != null) {
                    startTime = event.getStartTime();
                }
                if (event.getEndTime() != null) {
                    endTime = event.getEndTime();
                }

                eventEntry.setInterval(
                        LocalDateTime.of(startDate, startTime),
                        LocalDateTime.of(endDate, endTime)
                );

                calendar.addEntry(eventEntry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
