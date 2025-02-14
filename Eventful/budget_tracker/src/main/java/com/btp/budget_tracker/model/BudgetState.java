package com.btp.budget_tracker.model;

import java.util.List;

public class BudgetState {
    protected BudgetTracker currBudgetTracker;
    protected List<ExpenseEntry> currExpenses;
    protected ExpenseEntry currSelectedExpenseEntry;
}
