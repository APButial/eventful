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
        eventDetailsUI.getEventForm().getEndDatePicker().setValue(event.getEndDate());

        try {
            eventDetailsUI.getEventForm().getTimeStartField().getHourDropdown().setValue(event.getStartTime().format(DateTimeFormatter.ofPattern("HH")));
            eventDetailsUI.getEventForm().getTimeStartField().getMinuteDropdown().setValue(event.getStartTime().format(DateTimeFormatter.ofPattern("mm")));
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            eventDetailsUI.getEventForm().getTimeEndField().getHourDropdown().setValue(event.getEndTime().format(DateTimeFormatter.ofPattern("HH")));
            eventDetailsUI.getEventForm().getTimeEndField().getMinuteDropdown().setValue(event.getEndTime().format(DateTimeFormatter.ofPattern("mm")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            eventDetailsUI.getEventForm().getEventDescArea().setText(event.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            eventDetailsUI.getEventForm().getGuestEmailsArea().setText(String.join(";", event.getGuests()));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
