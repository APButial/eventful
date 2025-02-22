package com.btp.dashboard.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.time.LocalDate;

public class MyEvent {
    private VBox myEventList;

    public MyEvent() {
        myEventList = new VBox(10);
        myEventList.setPadding(new Insets(10, 20, 10, 20));

        // Header Section
        HBox header = new HBox(15);
        header.setPadding(new Insets(10, 15, 10, 15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #EAEAEA; -fx-font-weight: bold;");
        header.setMaxWidth(900);
// Header Sectio
// Set fixed widths for column headers
        // Set fixed widths for column headers
        Label eventNameHeader = new Label("Event Name");
        eventNameHeader.setPrefWidth(200);

        Label statusHeader = new Label("Status");
        statusHeader.setPrefWidth(100);

// Spacers to push Start Date & End Date backward
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        Label startDateHeader = new Label("Start Date");
        startDateHeader.setPrefWidth(120);

        Label endDateHeader = new Label("End Date");
        endDateHeader.setPrefWidth(120);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Label modifiedHeader = new Label("Date Modified");
        modifiedHeader.setPrefWidth(120);



        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(eventNameHeader, spacer, statusHeader, startDateHeader, endDateHeader, modifiedHeader);
        myEventList.getChildren().add(header);

        // Event List
        for (int i = 0; i < 5; i++) { // Simulating multiple events
            HBox event = new HBox(15);
            event.setPadding(new Insets(15));
            event.setAlignment(Pos.CENTER_LEFT);
            event.setStyle("-fx-background-color: #F5F6FA; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
            event.setMaxWidth(900);


            // Event Title and Date
            VBox eventDetails = new VBox(2);
            Label eventTitle = new Label("Event " + (i + 1));
            eventTitle.setFont(new Font(14));
            eventTitle.setStyle("-fx-font-weight: bold;");
            Label eventDate = new Label("Feb " + (i + 20) + " 12:00");
            eventDetails.getChildren().addAll(eventTitle, eventDate);

            // Sorting Icon
            Button sortButton = new Button("⬇⬆⬇");//i dont remember what this button is  settings?
            sortButton.setTextFill(Color.PURPLE);
            sortButton.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-padding: 2px;");
            sortButton.setPrefWidth(80);
            sortButton.setPrefHeight(20);

            // Status Dropdown with Styling
            ComboBox<String> statusDropdown = new ComboBox<>();
            statusDropdown.getItems().addAll("Draft", "On-Going", "Done", "Etc");
            statusDropdown.setValue("Draft");
            statusDropdown.setStyle(
                    "-fx-font-size: 10px; " +
                            "-fx-background-color: #A0A0A0; " +
                            "-fx-text-fill: white; " +
                            "-fx-border-radius: 5px; " +
                            "-fx-padding: 2px; "
            );
            statusDropdown.setButtonCell(new ListCell<>() {//added new import to edit the combobox
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? "" : item);
                    setTextFill(Color.WHITE); // Set selected text color to white
                }
            });

            statusDropdown.setPrefWidth(80);
            statusDropdown.setPrefHeight(25);
            statusDropdown.setMinWidth(80);
            statusDropdown.setMinHeight(25);
            statusDropdown.setMaxWidth(80);
            statusDropdown.setMaxHeight(25);

            // Start Date and End Date Pickers
            DatePicker startDatePicker = new DatePicker(LocalDate.now());
            startDatePicker.setPrefWidth(120);
            startDatePicker.setStyle("-fx-background-color: #8425A4; -fx-text-fill: white;");

            DatePicker endDatePicker = new DatePicker(LocalDate.now().plusDays(1));
            endDatePicker.setPrefWidth(120);
            endDatePicker.setStyle("-fx-background-color: #8425A4; -fx-text-fill: white;");

            // Last Modified Date
            Label modifiedDate = new Label("Feb 26 16:00");

            // Menu Button
            Button menuButton = new Button("⋮");
            menuButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px;");

            Region eventSpacer = new Region();
            HBox.setHgrow(eventSpacer, Priority.ALWAYS);

            event.getChildren().addAll( eventDetails, sortButton, eventSpacer, statusDropdown, startDatePicker, endDatePicker, modifiedDate, menuButton);
            StackPane eventWrapper = new StackPane(event);
            eventWrapper.setMaxWidth(900);
            myEventList.getChildren().add(eventWrapper);
        }
    }

    public VBox getComponent() {
        return myEventList;
    }
}
