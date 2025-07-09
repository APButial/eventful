package com.btp.event_manager.component;

import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.service.AppService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.time.LocalDate;

public class EventDetails {
    private VBox component;
    private Button cancelButton;
    private Button confirmButton;
    private TextField eventNameField;
    private CustomDatePicker startDatePicker;
    private CustomDatePicker endDatePicker;
    private AppService appService;
    private BaseEvent tempEvent;

    public EventDetails(AppService appService) {
        this.appService = appService;

        component = new VBox(10);
        component.setPadding(new Insets(20, 30, 40, 30)); // Reduced left padding
        component.setStyle("-fx-background-color: #FFFFFF;");

        // Purple Line Separator pushed far down
        Region largeSpacer = new Region();
        VBox.setVgrow(largeSpacer, Priority.ALWAYS);

        // Form Fields
        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);
        form.setAlignment(Pos.CENTER_LEFT); // Align form content to the left

        Label eventNameLabel = new Label("Event Name");
        eventNameField = new TextField();
        eventNameField.setPromptText("Enter event name");
        eventNameField.setPrefWidth(215);
        eventNameField.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: transparent; -fx-padding: 5px; -fx-border-radius: 5px;");
        eventNameField.setOnAction(event -> {
            appService.setEventName(eventNameField.getText());
        });

        ImageView nameIcon = new ImageView(new Image("/name.png"));
        nameIcon.setFitWidth(31);
        nameIcon.setPreserveRatio(true);

        Label startDateLabel = new Label("Start Date");
        startDatePicker = new CustomDatePicker(appService, false);
        startDatePicker.setPrefWidth(250);
        startDatePicker.setStyle("-fx-background-color: #F5F5F5; " +
                "-fx-border-color: transparent; " +
                "-fx-padding: 5px; " +
                "-fx-border-radius: 5px; " +
                "-fx-control-inner-background: #F5F5F5;" );
        startDatePicker.setPromptText("Select start date");
        startDatePicker.setValue(LocalDate.now()); // Default to today's date
        startDatePicker.setOnAction(event -> {
            appService.setStartDate(startDatePicker.getValue());
        });

        form.add(startDateLabel, 0, 2);
        form.add(new HBox(5, startDatePicker), 0, 3);

        Label endDateLabel = new Label("End Date");
        endDatePicker = new CustomDatePicker(appService, true);
        endDatePicker.setPrefWidth(250);
        endDatePicker.setStyle("-fx-background-color: #F5F5F5; " +
                "-fx-border-color: transparent; " +
                "-fx-padding: 5px; " +
                "-fx-border-radius: 5px; " +
                "-fx-control-inner-background: #F5F5F5;" );
        endDatePicker.setPromptText("Select end date");
        endDatePicker.setValue(LocalDate.now().plusDays(1)); // Default to tomorrow's date
        endDatePicker.setOnAction(event -> {
            appService.setEndDate(endDatePicker.getValue());
        });

        tempEvent = new BaseEvent(eventNameField.getText(), startDatePicker.getValue(), endDatePicker.getValue());
        appService.setSelectedEvent(tempEvent, false);
        appService.setStartDate(startDatePicker.getValue());
        appService.setEndDate(endDatePicker.getValue());
        appService.setEventName(eventNameField.getText());

        form.add(endDateLabel, 0, 4);
        form.add(new HBox(5, endDatePicker), 0, 5);

        form.add(eventNameLabel, 0, 0);
        form.add(new HBox(5, eventNameField, nameIcon), 0, 1);

        // Buttons
        cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: transparent; -fx-border-color: purple; -fx-text-fill: purple; -fx-border-radius: 5px; -fx-padding: 10px 20px;");
        cancelButton.setPrefSize(100, 10);

        confirmButton = new Button("Confirm");
        confirmButton.setStyle("-fx-background-color: purple; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px 20px;");
        confirmButton.setPrefSize(100, 10);

        HBox buttonBox = new HBox(20, cancelButton, confirmButton);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.setAlignment(Pos.BASELINE_LEFT); // Align buttons to the left

// Spacer to push buttons down
        Region buttonSpacer = new Region();
        buttonSpacer.setPrefHeight(50); // Adjust height to push buttons further down
        VBox.setVgrow(buttonSpacer, Priority.ALWAYS); // Allows expansion
// Add components in correct order
        component.getChildren().addAll(largeSpacer, form, buttonSpacer, buttonBox);

    }

    public VBox getComponent() {
        return component;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public TextField getEventNameField() {
        return eventNameField;
    }

    public CustomDatePicker getStartDatePicker() {
        return startDatePicker;
    }

    public CustomDatePicker getEndDatePicker() {
        return endDatePicker;
    }
}
