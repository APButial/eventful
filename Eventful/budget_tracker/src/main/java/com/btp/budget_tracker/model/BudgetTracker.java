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

        totalExpenses = 0.00;
        for (ExpenseEntry entry : this.expenses) {
            totalExpenses += (entry.getQuantity()* entry.getCostPerItem());
        }
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }
}
