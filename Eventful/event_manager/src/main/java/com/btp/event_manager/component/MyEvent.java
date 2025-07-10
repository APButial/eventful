package com.btp.event_manager.component;

import com.btp.appfx.enums.EventStatus;
import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.service.AppService;
import com.btp.event_manager.service.EventDetailListener;
import com.btp.event_manager.service.EventManAppService;
import com.btp.event_manager.service.WriteEventsService;
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
import javafx.util.StringConverter;

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

        filterEventsByStatus(appService.getEventFilter());
    }

    public VBox getComponent() {
        return myEventList;
    }

    private void filterEventsByStatus(EventStatus eventStatus) {
        if (myEventList != null) {
            myEventList.getChildren().clear();
        }

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

        HBox statusBox = new HBox(5);
        Label statusHeader = new Label("Status");

        Button statusFilterButton = new Button("⋮");
        statusFilterButton.setStyle("-fx-background-color: transparent; -fx-font-size: 8px;");
        statusFilterButton.setPrefWidth(20);
        statusFilterButton.setOnAction(e -> {
            if (eventStatus == null) {
                appService.setEventFilter(EventStatus.DRAFT);
            } else if (eventStatus == EventStatus.DRAFT) {
                appService.setEventFilter(EventStatus.PENDING);
            } else if (eventStatus == EventStatus.PENDING) {
                appService.setEventFilter(EventStatus.DONE);
            } else {
                appService.setEventFilter(null);
            }
            filterEventsByStatus(appService.getEventFilter());
            eventDetailListener.onFilterTriggered();
        });

        statusBox.getChildren().addAll(statusHeader, statusFilterButton);
        statusBox.setPrefWidth(80);

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

        header.getChildren().addAll(eventNameHeader, statusBox, startDateHeader, endDateHeader, modifiedHeader);
        myEventList.getChildren().add(header);

        Collections.sort(appService.getCurrUser().getEvents(), new Comparator<BaseEvent>() {
            @Override
            public int compare(BaseEvent e1, BaseEvent e2) {
                return e1.getEventName().compareTo(e2.getEventName());
            }
        });

        DateTimeFormatter fieldFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        StringConverter<LocalDate> stringConverter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? fieldFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, fieldFormatter) : null;
            }
        };

        for (BaseEvent event : appService.getCurrUser().getEvents()) { // Simulating multiple events
            if (eventStatus != null && event.getStatus() != eventStatus) {
                continue;
            }

            HBox eventBox = new HBox(15);
            eventBox.setPadding(new Insets(15));
            eventBox.setAlignment(Pos.CENTER_LEFT);
            eventBox.setStyle("-fx-background-color: #F5F6FA; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
            eventBox.setMaxWidth(900);

            // Event Title and Date
            Label eventTitle = new Label(event.getEventName());
            eventTitle.setFont(new Font(14));
            eventTitle.setPrefWidth(100);
            eventTitle.setMaxWidth(100);
            eventTitle.setStyle("-fx-font-weight: bold;");

            // Configure Icon
            Button configureButton = new Button("⬇⬆⬇");//i dont remember what this button is  settings?
            configureButton.setOnAction(e -> {
                appService.setSelectedEvent(event, true);
                eventDetailListener.onSelectEvent();
            });
            configureButton.setTextFill(Color.PURPLE);
            configureButton.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-padding: 2px;");
            configureButton.setPrefWidth(50);
            configureButton.setMaxWidth(50);
            configureButton.setPrefHeight(20);

            // Status Dropdown with Styling
            ComboBox<String> statusDropdown = new ComboBox<>();
            String status = appService.getEventStatusString(event.getStatus());

            statusDropdown.getItems().addAll("Draft", "Pending", "Done");
            statusDropdown.setValue(status);

            String draftStyle = "-fx-font-size: 10px;-fx-background-color: #D3D3D3;-fx-text-fill: white;-fx-border-radius: 5px;-fx-padding: 2px;";
            String pendingStyle = "-fx-font-size: 10px;-fx-background-color: #FFF25F;-fx-text-fill: white;-fx-border-radius: 5px;-fx-padding: 2px;";
            String doneStyle = "-fx-font-size: 10px;-fx-background-color: #90EE90;-fx-text-fill: white;-fx-border-radius: 5px;-fx-padding: 2px;";

            if (status.equals("Draft")) {
                statusDropdown.setStyle(draftStyle);
            } else if (status.equals("Pending")) {
                statusDropdown.setStyle(pendingStyle);
            } else if (status.equals("Done")) {
                statusDropdown.setStyle(doneStyle);
            }

            statusDropdown.setOnAction(e -> {
                String stat = statusDropdown.getValue();
                if (stat.equals("Draft")) {
                    statusDropdown.setStyle(draftStyle);
                } else if (stat.equals("Pending")) {
                    statusDropdown.setStyle(pendingStyle);
                } else if (stat.equals("Done")) {
                    statusDropdown.setStyle(doneStyle);
                }
                appService.setSelectedEvent(event, false);
                appService.setEventStatus(appService.getEventStatusEnum(stat));
                ((EventManAppService) appService)._statusUpdated();
                WriteEventsService.overwrite(appService, event.getStartDate(), event.getEndDate());
                appService.setSelectedEvent(null, false);
            });

            statusDropdown.setPrefWidth(90);
            statusDropdown.setPrefHeight(25);
            statusDropdown.setMaxWidth(90);
            statusDropdown.setMaxHeight(25);


            // Start Date and End Date Pickers
            DatePicker startDatePicker = new DatePicker(event.getStartDate());
            startDatePicker.setDisable(true);
            startDatePicker.setEditable(false);
            startDatePicker.setOpacity(1);
            startDatePicker.getEditor().setOpacity(1);
            startDatePicker.setPrefWidth(120);
            startDatePicker.setStyle("-fx-background-color: #8425A4; -fx-text-fill: white;");
            startDatePicker.setConverter(stringConverter);

            DatePicker endDatePicker = new DatePicker(event.getEndDate());
            endDatePicker.setDisable(true);
            endDatePicker.setEditable(false);
            endDatePicker.setOpacity(1);
            endDatePicker.getEditor().setOpacity(1);
            endDatePicker.setPrefWidth(120);
            endDatePicker.setStyle("-fx-background-color: #8425A4; -fx-text-fill: white;");
            endDatePicker.setConverter(stringConverter);

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

            eventBox.getChildren().addAll(eventTitle, configureButton, eventSpacer, statusDropdown, startDatePicker, endDatePicker, modifiedDate, menuButton);
            StackPane eventWrapper = new StackPane(eventBox);
            eventWrapper.setMaxWidth(900);
            myEventList.getChildren().add(eventWrapper);
        }
    }
}
