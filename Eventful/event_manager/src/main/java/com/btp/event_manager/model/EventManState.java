package com.btp.event_manager.model;

import com.btp.appfx.model.AppState;
import com.btp.budget_tracker.model.BudgetTracker;
import com.btp.budget_tracker.model.ExpenseEntry;
import lombok.Data;

import java.util.List;

@Data
public class EventManState extends AppState {
    protected BudgetTracker currBudgetTracker;
    protected ExpenseEntry currSelectedExpenseEntry;
    protected List<ExpenseEntry> currExpenses;


}
