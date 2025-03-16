package com.btp.budget_tracker.model;

import java.util.ArrayList;
import java.util.List;

public class BudgetTracker {
    private List<ExpenseEntry> expenses;
    private Double totalExpenses;

    public BudgetTracker() {
        this.expenses = new ArrayList<>();
    }

    public List<ExpenseEntry> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseEntry> expenses) {
        this.expenses = expenses;
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
}
