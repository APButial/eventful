package com.btp.event_manager.model;

import com.btp.appfx.model.AppState;
import com.btp.budget.model.BudgetTracker;
import com.btp.budget.model.ExpenseEntry;
import com.btp.event_manager.service.DateTimeListener;
import com.btp.event_manager.service.DateTimeService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventManState extends AppState {
    protected BudgetTracker currBudgetTracker;
    protected ExpenseEntry currSelectedExpenseEntry;
    protected List<ExpenseEntry> currExpenses;
    protected DateTimeService dateTimeService;
    protected File metadata;
    protected String directory;
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

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public File getMetadata() {
        return metadata;
    }

    public void setMetadata(File metadata) {
        this.metadata = metadata;
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
