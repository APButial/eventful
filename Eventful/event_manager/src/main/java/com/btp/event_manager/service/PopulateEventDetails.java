package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.component.EventDetailsUI;
import com.btp.event_manager.model.Event;
import javafx.scene.control.DatePicker;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PopulateEventDetails {
    public static void populate(AppService appService, EventDetailsUI eventDetailsUI) {
        Event event = (Event) appService.getSelectedEvent();

        eventDetailsUI.getEventForm().getStartDatePicker().setValue(event.getStartDate());
        appService.setStartDate(event.getStartDate());
        eventDetailsUI.getEventForm().getEndDatePicker().setValue(event.getEndDate());
        appService.setEndDate(event.getEndDate());

        try {
            eventDetailsUI.getEventForm().getTimeStartField().getHourDropdown().setValue(event.getStartTime().format(DateTimeFormatter.ofPattern("HH")));
            eventDetailsUI.getEventForm().getTimeStartField().getMinuteDropdown().setValue(event.getStartTime().format(DateTimeFormatter.ofPattern("mm")));
            appService.setStartTime(event.getStartTime());
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            eventDetailsUI.getEventForm().getTimeEndField().getHourDropdown().setValue(event.getEndTime().format(DateTimeFormatter.ofPattern("HH")));
            eventDetailsUI.getEventForm().getTimeEndField().getMinuteDropdown().setValue(event.getEndTime().format(DateTimeFormatter.ofPattern("mm")));
            appService.setEndTime(event.getEndTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            eventDetailsUI.getEventForm().getEventDescArea().setText(event.getDescription());
            appService.setDescription(event.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            eventDetailsUI.getEventForm().getGuestEmailsArea().setText(String.join(";", event.getGuests()));
            if(!eventDetailsUI.getEventForm().getGuestEmailsArea().getText().isEmpty()) {
                eventDetailsUI.getEventForm().getGuestEmailsArea().setText(eventDetailsUI.getEventForm().getGuestEmailsArea().getText() + ";");
                appService.setGuests(appService.getGuests());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
