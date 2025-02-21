package com.btp.dashboard.component;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MilitaryTimePicker extends HBox {

    private ComboBox<String> hourDropdown;
    private ComboBox<String> minuteDropdown;

    public MilitaryTimePicker() {
        // Hour Dropdown (0-23 for military time)
        hourDropdown = new ComboBox<>();
        for (int i = 0; i < 24; i++) {
            hourDropdown.getItems().add(String.format("%02d", i));  // Add 00-23 hours
        }
        hourDropdown.setValue("00");  // Default to 00

        // Minute Dropdown (0-59 for minutes)
        minuteDropdown = new ComboBox<>();
        for (int i = 0; i < 60; i++) {
            minuteDropdown.getItems().add(String.format("%02d", i));  // Add 00-59 minutes
        }
        minuteDropdown.setValue("00");  // Default to 00

        hourDropdown.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 0px; " +
                        "-fx-font-size: 10px; "
        );

        minuteDropdown.setStyle(
                "-fx-background-color: transparent; " +  // Remove background color
                        "-fx-border-color: transparent; " +      // Remove border color
                        "-fx-padding: 0px; " +                    // Remove padding
                        "-fx-font-size: 10px; "                   // Adjust font size (if needed)
        );

        // Layout for the time picker with dropdowns
        this.setSpacing(0);  // Remove spacing between components
        this.setAlignment(Pos.CENTER_LEFT);  // Align components to the left
        this.setPrefHeight(30);  // Set height if necessary to match the icon height

        // Add hour dropdown, colon separator, and minute dropdown
        this.getChildren().addAll(hourDropdown, new Label(":"), minuteDropdown);
    }

    public ComboBox<String> getHourDropdown() {
        return hourDropdown;
    }

    public ComboBox<String> getMinuteDropdown() {
        return minuteDropdown;
    }

    public void setHourDropdown(ComboBox<String> hourDropdown) {
        this.hourDropdown = hourDropdown;
    }

    public void setMinuteDropdown(ComboBox<String> minuteDropdown) {
        this.minuteDropdown = minuteDropdown;
    }
}