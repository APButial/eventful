package com.btp.dashboard.component;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomDatePicker extends DatePicker {

    public CustomDatePicker() {
        // Set placeholder text
        this.setPromptText("MM/DD/YYYY");

        // Apply styling
        this.setStyle("-fx-background-color: #F5F5F5; " +
                "-fx-border-color: transparent; " +
                "-fx-padding: 5px; " +
                "-fx-border-radius: 5px; " +
                "-fx-control-inner-background: #F5F5F5;" );

        this.setEditable(false); // Prevent manual input
        this.getStyleClass().add("custom-datepicker"); // Apply CSS styles

        // Set date format
        DateTimeFormatter fieldFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        this.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? date.format(fieldFormatter) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, fieldFormatter) : null;
            }
        });

        // Close popup when a date is selected
        this.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    this.setOnMouseClicked(e -> {
                        hide();
                        setValue(item);
                    });
                }
            }
        });
    }
}
