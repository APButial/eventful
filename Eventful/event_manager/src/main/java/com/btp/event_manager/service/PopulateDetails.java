package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import com.btp.budget.model.BudgetTracker;
import com.btp.budget.model.ExpenseEntry;
import com.btp.event_manager.component.BudgetTrackerUI;
import com.btp.event_manager.component.EventDetailsUI;
import com.btp.event_manager.model.Event;

import java.time.format.DateTimeFormatter;

public class PopulateDetails {
    public static void populateEventDetails(AppService appService, EventDetailsUI eventDetailsUI) {
        Event event = (Event) appService.getSelectedEvent();

        eventDetailsUI.getEventForm().getStartDatePicker().setValue(event.getStartDate());
        appService.setStartDate(event.getStartDate());
        ((EventManAppService) appService).getEventManState().setStartDateBuffer(event.getStartDate());
        eventDetailsUI.getEventForm().getEndDatePicker().setValue(event.getEndDate());
        appService.setEndDate(event.getEndDate());
        ((EventManAppService) appService).getEventManState().setEndDateBuffer(event.getEndDate());
        event.setLastAccessed(appService.getSysDateTime());
        eventDetailsUI.getStatusBox().setValue(appService.getEventStatusString(event.getStatus()));
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
            appService.setEventID(event.getEventID());
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

    public static void populateBudgetTracker(AppService appService, BudgetTrackerUI budgetTrackerUI) {
        Event event = (Event) appService.getSelectedEvent();
        BudgetTracker budgetTracker = event.getBudgetTracker();
        budgetTrackerUI.clearTable();

        if (budgetTracker == null) {
            return;
        }
        ((EventManAppService) appService).setBudgetTracker(budgetTracker);

        int quantity;
        String itemName;
        double costPerItem;

        for (ExpenseEntry entry : budgetTracker.getExpenses()) {
            quantity = entry.getQuantity();
            itemName = entry.getItemName();
            costPerItem = entry.getCostPerItem();

            budgetTrackerUI.getBudgetTable().addRow(new ExpenseEntry(quantity, itemName, costPerItem));
        }
    }
}
