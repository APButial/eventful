package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.component.EventDetailsUI;
import com.btp.event_manager.model.Event;
import javafx.scene.control.DatePicker;

public class PopulateEventDetails {
    public static void populate(AppService appService, EventDetailsUI eventDetailsUI) {
        Event event = (Event) appService.getSelectedEvent();

        eventDetailsUI.getEventForm().getStartDatePicker().setValue(event.getStartDate());
        eventDetailsUI.getEventForm().getEndDatePicker().setValue(event.getEndDate());
        eventDetailsUI.getEventForm().getEventDescArea().setText(String.join(";", appService.getGuests()));
    }
}
