package com.btp.budget_tracker.model;

import lombok.Data;

import java.util.List;

@Data
public class BudgetTracker {
    private List<ExpenseEntry> expenses;
    private Double totalExpenses;


}
