package com.btp.event_manager.component;

import com.btp.appfx.enums.EventStatus;
import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.service.AppService;
import com.btp.event_manager.service.EventDetailListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class EventList {
    private VBox eventList;
    private AppService appService;
    private EventDetailListener eventDetailListener;

    public EventList(AppService appService, EventDetailListener eventDetailListener) {
        this.appService = appService;
        this.eventDetailListener = eventDetailListener;
        eventList = new VBox(10);
        eventList.setPadding(new Insets(10, 20, 10, 20));

        // sorts events in descending order by their start date
        Collections.sort(appService.getCurrUser().getEvents(), new Comparator<BaseEvent>() {
            @Override
            public int compare(BaseEvent e1, BaseEvent e2) {
                return e2.getStartDate().compareTo(e1.getStartDate());
            }
        });

        LocalDate maxDate = appService.getSysDateTime().toLocalDate().plusDays(7); // 1 week
        for (BaseEvent event : appService.getCurrUser().getEvents()) {
            // show events within the week
            if (event.getStartDate().isAfter(maxDate) || event.getStartDate().isBefore(appService.getSysDateTime().toLocalDate())
                || event.getStatus() == EventStatus.DONE
            ) {continue;}
            HBox eventBox = new HBox(20);
            VBox eventText = new VBox();
            eventBox.setPadding(new Insets(15));
            eventBox.setAlignment(Pos.CENTER_LEFT);
            eventBox.setStyle("-fx-background-color: #F5F6FA; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
            eventBox.setMaxWidth(900);

            Label eventTitle = new Label(event.getEventName());
            eventTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            String startDate = "Starts at " +
                    event.getStartDate().getMonth().getDisplayName(TextStyle.SHORT,Locale.ENGLISH) + " " +
                    event.getStartDate().getDayOfMonth() + " " + event.getStartDate().getYear();
            Label eventStart = new Label(startDate);
            eventStart.setStyle("-fx-font-size: 14px; -fx-text-fill: #AAB2C8");
            eventText.getChildren().addAll(eventTitle, eventStart);

            Button guestsButton = new Button("👥 " + event.getGuests().size());
            Button settingsButton = new Button("⚙ Configure");
            settingsButton.setOnAction(event1 -> {
                appService.setSelectedEvent(event, true);
                eventDetailListener.onSelectEvent();
            });
            Button statusButton = new Button("📊 Status");
            Button menuButton2 = new Button("⋮");

            String textButtonStyle2 = "-fx-background-color: transparent; -fx-border: none; -fx-font-size: 14px; -fx-text-fill: #8425A4;";
            guestsButton.setStyle(textButtonStyle2);
            settingsButton.setStyle(textButtonStyle2);
            statusButton.setStyle(textButtonStyle2);
            menuButton2.setStyle(textButtonStyle2);

            Region eventSpacer = new Region();
            HBox.setHgrow(eventSpacer, Priority.ALWAYS);

            eventBox.getChildren().addAll(eventText, eventSpacer, guestsButton, settingsButton, statusButton, menuButton2);

            StackPane eventWrapper = new StackPane(eventBox);
            eventWrapper.setMaxWidth(900);
            eventList.getChildren().add(eventWrapper);
        }
    }

    public VBox getComponent() {
        return eventList;
    }
}
