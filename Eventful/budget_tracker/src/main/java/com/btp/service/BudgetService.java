package com.btp.service;

import com.btp.model.BudgetTracker;
import com.btp.model.ExpenseEntry;

import java.util.List;

public interface BudgetService {
    void addBudgetTracker();
    void removeBudgetTracker();
    void addExpenseEntry(ExpenseEntry expenseEntry);
    void removeExpenseEntry(ExpenseEntry expenseEntry);
    BudgetTracker getBudgetTracker();
    void setBudgetTracker(BudgetTracker budgetTracker);
    ExpenseEntry getExpenseEntry();
    void setExpenseEntry(ExpenseEntry expenseEntry);
    List<ExpenseEntry> getExpenses();
    ExpenseEntry getSelectedExpenseEntry();
    void setSelectedExpenseEntry();
    void exportBudgetTracker(BudgetTracker budgetTracker);
}
