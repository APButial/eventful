package com.btp.dashboard.component;

import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.service.AppService;
import com.btp.dashboard.service.EventDetailListener;
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
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MyEvent {
    private VBox myEventList;
    private AppService appService;
    private EventDetailListener eventDetailListener;

    public MyEvent(AppService appService, EventDetailListener eventDetailListener) {
        this.appService = appService;
        this.eventDetailListener = eventDetailListener;

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
        eventNameHeader.setPrefWidth(180);

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
        modifiedHeader.setPrefWidth(140);



        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(eventNameHeader, statusHeader, startDateHeader, endDateHeader, modifiedHeader);
        myEventList.getChildren().add(header);

        Collections.sort(appService.getCurrUser().getEvents(), new Comparator<BaseEvent>() {
            @Override
            public int compare(BaseEvent e1, BaseEvent e2) {
                return e1.getEventName().compareTo(e2.getEventName());
            }
        });

        // Event List
        for (BaseEvent event : appService.getCurrUser().getEvents()) { // Simulating multiple events
            HBox eventBox = new HBox(15);
            eventBox.setPadding(new Insets(15));
            eventBox.setAlignment(Pos.CENTER_LEFT);
            eventBox.setStyle("-fx-background-color: #F5F6FA; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
            eventBox.setMaxWidth(900);


            // Event Title and Date
            Label eventTitle = new Label(event.getEventName());
            eventTitle.setFont(new Font(14));
            eventTitle.setPrefWidth(100);
            eventTitle.setStyle("-fx-font-weight: bold;");

            // Configure Icon
            Button configureButton = new Button("⬇⬆⬇");//i dont remember what this button is  settings?
            configureButton.setOnAction(e -> {
                appService.setSelectedEvent(event);
                eventDetailListener.onSelectEvent();
            });
            configureButton.setTextFill(Color.PURPLE);
            configureButton.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-padding: 2px;");
            configureButton.setPrefWidth(50);
            configureButton.setPrefHeight(20);

            // Status Dropdown with Styling
            ComboBox<String> statusDropdown = new ComboBox<>();
            statusDropdown.getItems().addAll("Draft", "On-Going", "Done");
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

            statusDropdown.setPrefWidth(70);
            statusDropdown.setPrefHeight(25);
            statusDropdown.setMinWidth(70);
            statusDropdown.setMinHeight(25);
            statusDropdown.setMaxWidth(70);
            statusDropdown.setMaxHeight(25);

            // Start Date and End Date Pickers
            DatePicker startDatePicker = new DatePicker(event.getStartDate());
            startDatePicker.setDisable(true);
            startDatePicker.setEditable(false);
            startDatePicker.setOpacity(1);
            startDatePicker.getEditor().setOpacity(1);
            startDatePicker.setPrefWidth(120);
            startDatePicker.setStyle("-fx-background-color: #8425A4; -fx-text-fill: white;");

            DatePicker endDatePicker = new DatePicker(event.getEndDate());
            endDatePicker.setDisable(true);
            endDatePicker.setEditable(false);
            endDatePicker.setOpacity(1);
            endDatePicker.getEditor().setOpacity(1);
            endDatePicker.setPrefWidth(120);
            endDatePicker.setStyle("-fx-background-color: #8425A4; -fx-text-fill: white;");

            // Last Modified Date
            Label modifiedDate = new Label(event.getLastAccessed().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " +
                                              event.getLastAccessed().getDayOfMonth() + " " +
                                              event.getLastAccessed().format(DateTimeFormatter.ofPattern("HH:mm")));
            modifiedDate.setStyle("-fx-text-fill: #AAB2C8");

            // Menu Button
            Button menuButton = new Button("⋮");
            menuButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px;");

            Region eventSpacer = new Region();
            HBox.setHgrow(eventSpacer, Priority.ALWAYS);

            eventBox.getChildren().addAll( eventTitle, configureButton, eventSpacer, statusDropdown, startDatePicker, endDatePicker, modifiedDate, menuButton);
            StackPane eventWrapper = new StackPane(eventBox);
            eventWrapper.setMaxWidth(900);
            myEventList.getChildren().add(eventWrapper);
        }
    }

    public VBox getComponent() {
        return myEventList;
    }
}
