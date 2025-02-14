package com.btp.event_manager.model;

import com.btp.appfx.model.AppState;
import com.btp.budget_tracker.model.BudgetTracker;
import com.btp.budget_tracker.model.ExpenseEntry;
import com.btp.event_manager.service.DateTimeListener;
import com.btp.event_manager.service.DateTimeService;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class EventManState extends AppState {
    protected BudgetTracker currBudgetTracker;
    protected ExpenseEntry currSelectedExpenseEntry;
    protected List<ExpenseEntry> currExpenses;
    protected DateTimeService dateTimeService;

    public EventManState() {
        dateTimeService = new DateTimeService(new DateTimeListener() {
            @Override
            public void updateDateTime(LocalDateTime localDateTime) {
                currDateTime = localDateTime;
            }
        });
        currExpenses = new ArrayList<>();
    }
}
