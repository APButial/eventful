package com.btp.event_manager.model;

import com.btp.appfx.model.BaseEvent;
import com.btp.budget.model.BudgetTracker;

import java.time.LocalDate;

public class Event extends BaseEvent {
    private BudgetTracker budgetTracker;

    public Event(String eventName, LocalDate startDate, LocalDate endDate) {
        super(eventName, startDate, endDate);
    }

    public BudgetTracker getBudgetTracker() {
        return budgetTracker;
    }

    public void setBudgetTracker(BudgetTracker budgetTracker) {
        this.budgetTracker = budgetTracker;
    }
}
