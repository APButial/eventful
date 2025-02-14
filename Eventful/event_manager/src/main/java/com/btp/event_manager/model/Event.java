package com.btp.event_manager.model;

import com.btp.appfx.model.BaseEvent;
import com.btp.budget_tracker.model.BudgetTracker;

import java.time.LocalDate;
import java.util.List;

public class Event extends BaseEvent {
    private BudgetTracker budgetTracker;

    Event(String eventName, LocalDate startDate, LocalDate endDate) {
        super(eventName, startDate, endDate);
    }
}
