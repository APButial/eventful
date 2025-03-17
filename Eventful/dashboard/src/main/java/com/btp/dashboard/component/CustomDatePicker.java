package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;

public class CustomDatePicker extends DatePicker {
    private boolean isEndDate;
    private AppService appService;

    public CustomDatePicker(AppService appService, Boolean isEndDate) {
        this.appService = appService;
        this.isEndDate = isEndDate;

        // Set placeholder text
        this.setPromptText("");

        // Apply styling
        this.setStyle("-fx-background-color: #F5F5F5; " +
                "-fx-border-color: transparent; " +
                "-fx-padding: 5px; " +
                "-fx-border-radius: 5px; " +
                "-fx-control-inner-background: #F5F5F5;" +
                "-fx-focus-color: #8425a4");


        this.setEditable(false); // Prevent manual input
        this.getStyleClass().add("date-picker"); // Apply CSS styles

        // Set date format
        DateTimeFormatter fieldFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        this.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? fieldFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, fieldFormatter) : null;
            }
        });

        // Close popup when a date is selected
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now()) || (isEndDate && item.isBefore(appService.getStartDate()))) {
                            this.setDisable(true);
                            this.setStyle("-fx-background-color: #d3d3d3;"); // Optional: Change background color for disabled dates
                        }
                    }
                };
            }
        };
        setDayCellFactory(dayCellFactory);
    }
}
