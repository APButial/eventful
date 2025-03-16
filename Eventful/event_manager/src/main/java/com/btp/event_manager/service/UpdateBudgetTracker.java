package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import com.btp.budget.model.BudgetTracker;
import com.btp.budget.model.ExpenseEntry;
import com.btp.dashboard.component.BudgetTrackerUI;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class UpdateBudgetTracker {
    public static boolean validate(AppService appService, BudgetTrackerUI budgetTrackerUI) {
        List<ExpenseEntry> expenses = budgetTrackerUI.getBudgetTable().getExpenses();
        List<ExpenseEntry> uniqueExpenses = new ArrayList<>(); // checks if expense item names are unique
        for (ExpenseEntry entry : expenses) {
            if (entry.getQuantity() < 0) {
                negativeQuantity();
                return false;
            }
            if (entry.getCostPerItem() < 0) {
                negativeCostPerItem();
                return false;
            }
            if (entry.getItemName().isBlank()) {
                itemNameIsBlank();
                return false;
            }
            if (entry.getQuantity() == 0 && entry.getCostPerItem() > 0) { // quantity must not be 0 if there is cost per item
                invalidCostPerItem();
                return false;
            }

            for (ExpenseEntry entry2 : uniqueExpenses) {
                if (entry2.getItemName().equals(entry.getItemName())) { // item name already exists
                    itemNotUnique();
                    return false;
                }
            }

            uniqueExpenses.add(entry);
        }

        BudgetTracker newBudgetTracker = new BudgetTracker();
        newBudgetTracker.setExpenses(uniqueExpenses);

        ((EventManAppService) appService).setBudgetTracker(newBudgetTracker);
        return true;
    }

    private static void negativeQuantity() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Expenses Tracker");
        alert.setHeaderText("Invalid Quantity");
        alert.setContentText("Quantity must not be less than 0.");
        alert.showAndWait();
    }

    private static void negativeCostPerItem() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Expenses Tracker");
        alert.setHeaderText("Invalid CostPerItem");
        alert.setContentText("Cost per item must not be less than 0.");
        alert.showAndWait();
    }

    private static void itemNameIsBlank() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Expenses Tracker");
        alert.setHeaderText("Incomplete Expenses' Details");
        alert.setContentText("Expenses' item names must not be blank or empty.");
        alert.showAndWait();
    }

    private static void invalidCostPerItem() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Expenses Tracker");
        alert.setHeaderText("Invalid Expense Item Quantity");
        alert.setContentText("Quantity must be greater than zero if cost per item is not zero.");
        alert.showAndWait();
    }

    private static void itemNotUnique() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Expenses Tracker");
        alert.setHeaderText("Invalid Item Name");
        alert.setContentText("Item names must be unique.");
        alert.showAndWait();
    }
}
