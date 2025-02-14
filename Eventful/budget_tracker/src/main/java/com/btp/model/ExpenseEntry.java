package com.btp.model;

import lombok.Data;

@Data
public class ExpenseEntry {
    private int quantity; // must be greater than or equal to 1
    private String itemName;
    private Double cost; // should not be negative
}
