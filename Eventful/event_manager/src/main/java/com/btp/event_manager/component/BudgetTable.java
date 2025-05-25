package com.btp.event_manager.component;

import com.btp.budget.model.ExpenseEntry;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.List;

public class BudgetTable {
    private VBox component;
    private VBox rowsContainer;
    private List<HBox> rows;
    private List<ExpenseEntry> expenses;
    private TextField totalSumField;


    public List<ExpenseEntry> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseEntry> expenses) {
        this.expenses = expenses;
    }

    public TextField getTotalSumField() {
        return totalSumField;
    }

    public void setTotalSumField(TextField totalSumField) {
        this.totalSumField = totalSumField;
    }

    private static final double QUANTITY_WIDTH = 100;
    private static final double ITEM_WIDTH = 200;
    private static final double COST_WIDTH = 150;
    private static final double TOTAL_WIDTH = 150;

    public BudgetTable() {
        component = new VBox(10);
        component.setPadding(new Insets(15));
        component.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 15px;");

        expenses = new ArrayList<>();
        rowsContainer = new VBox(5);
        rows = new ArrayList<>();

        HBox header = createHeader();
        HBox totalRow = createTotalRow();
        HBox addButtonRow = createAddButtonRow();

        component.getChildren().addAll(header, rowsContainer, totalRow, addButtonRow);
    }

    public void addRow(ExpenseEntry expenseEntry) {
        expenses.add(expenseEntry);

        HBox row = createRow(expenseEntry);
        rows.add(row);
        rowsContainer.getChildren().add(row);
    }

    private HBox createHeader() {
        HBox header = new HBox(10);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #F8F8FA; -fx-border-radius: 10px; -fx-border-color: white;");
        header.setAlignment(Pos.CENTER);

        Label quantityLabel = createCenteredLabel("Quantity", QUANTITY_WIDTH);
        Label itemLabel = createCenteredLabel("Item", ITEM_WIDTH);
        Label costLabel = createCenteredLabel("Cost per item (₱)", COST_WIDTH);
        Label totalLabel = createCenteredLabel("Total (₱)", TOTAL_WIDTH);

        header.getChildren().addAll(quantityLabel, itemLabel, costLabel, totalLabel);
        return header;
    }

    private Label createCenteredLabel(String text, double width) {
        Label label = new Label(text);
        label.setMinWidth(width);
        label.setPrefWidth(width);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        return label;
    }

    private HBox createRow(ExpenseEntry expenseEntry) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(10));
        row.setStyle("-fx-background-color: #F8F8FA; -fx-border-radius: 10px; -fx-border-color: white;");
        row.setAlignment(Pos.CENTER);

        TextField quantityField = createCenteredTextField(String.valueOf(expenseEntry.getQuantity()), QUANTITY_WIDTH);
        TextField itemField = createCenteredTextField(expenseEntry.getItemName(), ITEM_WIDTH);
        TextField costField = createCenteredTextField(String.format("%.2f", expenseEntry.getCostPerItem()), COST_WIDTH);
        TextField totalField = createCenteredTextField(String.format("%.2f", expenseEntry.getQuantity()*expenseEntry.getCostPerItem()), TOTAL_WIDTH);
        totalField.setEditable(false);

        quantityField.setOnKeyReleased(e -> updateExpenseEntry(expenseEntry, quantityField, itemField, costField, totalField));
        costField.setOnKeyReleased(e -> updateExpenseEntry(expenseEntry, quantityField, itemField, costField, totalField));
        itemField.setOnKeyReleased(e -> updateExpenseEntry(expenseEntry, quantityField, itemField, costField, totalField));

        row.getChildren().addAll(quantityField, itemField, costField, totalField);
        updateTotalSum();
        return row;
    }

    private TextField createCenteredTextField(String initialText, double width) {
        TextField textField = new TextField(initialText);
        textField.setMinWidth(width);
        textField.setPrefWidth(width);
        textField.setAlignment(Pos.CENTER);
        textField.setStyle("-fx-font-size: 14px;");
        return textField;
    }

    private void updateExpenseEntry(ExpenseEntry expenseEntry, TextField quantityField, TextField itemField, TextField costField, TextField totalField) {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            double cost = Double.parseDouble(costField.getText());
            String itemName = itemField.getText();

            if(!itemName.isBlank())
                itemName = itemName.strip();

            expenseEntry.setQuantity(quantity);
            expenseEntry.setCostPerItem(cost);
            expenseEntry.setItemName(itemName);

            double total = expenseEntry.getTotalCost();
            totalField.setText(String.format("%.2f", total));
        } catch (NumberFormatException ignored) {
        }
        updateTotalSum();
    }

    private void updateTotalSum() {
        double total = expenses.stream()
                .mapToDouble(ExpenseEntry::getTotalCost)
                .sum();
        totalSumField.setText(String.format("%.2f", total));
    }

    private HBox createTotalRow() {
        HBox totalRow = new HBox(10);
        totalRow.setPadding(new Insets(10));
        totalRow.setAlignment(Pos.CENTER_RIGHT);

        Label totalLabel = new Label("Total Sum");
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        totalSumField = createCenteredTextField("0.00", TOTAL_WIDTH);
        totalSumField.setEditable(false);

        totalRow.getChildren().addAll(totalLabel, totalSumField);
        return totalRow;
    }

    private HBox createAddButtonRow() {
        HBox addButtonRow = new HBox();
        addButtonRow.setPadding(new Insets(10));
        addButtonRow.setAlignment(Pos.CENTER);

        Button addButton = new Button("+");
        addButton.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 2px; " +
                        "-fx-min-width: 24px; " +
                        "-fx-min-height: 24px; " +
                        "-fx-text-fill: black;"
        );

        addButton.setOnAction(e -> addRow(new ExpenseEntry()));

        addButtonRow.getChildren().add(addButton);
        return addButtonRow;
    }

    public VBox getComponent() {
        return component;
    }
}