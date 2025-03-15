package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomDatePicker extends DatePicker {
    private LocalDate selectedDate;
    private boolean isEndDate;
    private AppService appService;

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public CustomDatePicker(AppService appService, Boolean isEndDate) {
        this.appService = appService;
        this.isEndDate = isEndDate;

        // Set placeholder text
        this.setPromptText("MM/DD/YYYY");

        // Apply styling
        this.setStyle("-fx-background-color: #F5F5F5; " +
                "-fx-border-color: transparent; " +
                "-fx-padding: 5px; " +
                "-fx-border-radius: 5px; " +
                "-fx-control-inner-background: #F5F5F5;");


        this.setEditable(false); // Prevent manual input
        this.getStyleClass().add("custom-datepicker"); // Apply CSS styles

        // Set date format
        DateTimeFormatter fieldFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
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
                    if (item.isBefore(LocalDate.now()) || (isEndDate && item.isBefore(appService.getStartDate()))) {
                        this.setDisable(true);
                        this.setStyle("-fx-background-color: #d3d3d3;"); // Optional: Change background color for disabled dates
                    } else {
                        if (item.equals(selectedDate)) {
                            this.setStyle("-fx-background-color: #8425a4; -fx-text-fill: #ffffff");
                        }
                        this.setOnMouseClicked(e -> {
                            hide();
                            selectedDate = item;
                            setValue(item);
                        });
                        this.setOnMouseEntered(e -> {
                            this.setStyle("-fx-background-color: #8425a4; -fx-text-fill: #ffffff");
                        });
                        this.setOnMouseExited(e -> {
                            if (!item.equals(selectedDate))
                                this.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #000000");
                        });
                    }
                }
            }
        });
    }
}
