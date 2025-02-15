package com.btp.event_manager.model;

import com.btp.appfx.model.AppState;
import com.btp.budget_tracker.model.BudgetTracker;
import com.btp.budget_tracker.model.ExpenseEntry;
import com.btp.dashboard.service.DateTimeListener;
import com.btp.dashboard.service.DateTimeService;
import javafx.application.Application;
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
    protected Application prevApplication;

    public EventManState() {
        dateTimeService = new DateTimeService(new DateTimeListener() {
            @Override
            public void updateDateTime(LocalDateTime localDateTime) {
                currDateTime = localDateTime;
            }
        });
        currExpenses = new ArrayList<>();
        setLoggedIn(false);
    }

    public void setPrevApplication(Application prevApplication) {
        this.prevApplication = prevApplication;
    }
    public Application getPrevApplication() {
        return this.prevApplication;
    }
}
