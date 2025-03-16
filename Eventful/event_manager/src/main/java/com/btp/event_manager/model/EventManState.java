package com.btp.event_manager.model;

import com.btp.appfx.model.AppState;
import com.btp.budget_tracker.model.BudgetTracker;
import com.btp.budget_tracker.model.ExpenseEntry;
import com.btp.dashboard.service.DateTimeListener;
import com.btp.dashboard.service.DateTimeService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventManState extends AppState {
    protected BudgetTracker currBudgetTracker;
    protected ExpenseEntry currSelectedExpenseEntry;
    protected List<ExpenseEntry> currExpenses;
    protected DateTimeService dateTimeService;
    protected Application prevApplication;
    protected Stage mainStage;

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

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public BudgetTracker getCurrBudgetTracker() {
        return currBudgetTracker;
    }

    public void setCurrBudgetTracker(BudgetTracker currBudgetTracker) {
        this.currBudgetTracker = currBudgetTracker;
        ((Event) currSelectedEvent).setBudgetTracker(currBudgetTracker);
    }
}
