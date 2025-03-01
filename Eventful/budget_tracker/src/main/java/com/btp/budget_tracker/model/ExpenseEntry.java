package com.btp.budget_tracker.model;

import lombok.Data;

@Data
public class ExpenseEntry {
    private int quantity;
    private String itemName;
    private double costPerItem;

    public ExpenseEntry() {
        this.quantity = 1;
        this.itemName = "";
        this.costPerItem = 0.0;
    }

    public ExpenseEntry(int quantity, String itemName, double costPerItem) {
        this.quantity = Math.max(quantity, 1);
        this.itemName = itemName;
        this.costPerItem = Math.max(costPerItem, 0.0);
    }

    public double getTotalCost() {
        return quantity * costPerItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getCostPerItem() {
        return costPerItem;
    }

    public void setCostPerItem(double costPerItem) {
        this.costPerItem = costPerItem;
    }
}
