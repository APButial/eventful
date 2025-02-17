package com.btp.dashboard.component;

import com.btp.appfx.model.BaseEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

import java.util.List;

public class EventList {
    private VBox eventList;

    public EventList(List<BaseEvent> events) {
        eventList = new VBox(10);
        eventList.setPadding(new Insets(10, 20, 10, 20));

        for (BaseEvent event : events) { // Simulating multiple events
            HBox eventBox = new HBox(20);
            eventBox.setPadding(new Insets(15));
            eventBox.setAlignment(Pos.CENTER_LEFT);
            eventBox.setStyle("-fx-background-color: #F5F6FA; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
            eventBox.setMaxWidth(900);

            Label eventTitle = new Label(event.getEventName() + "\n" + event.getStartDate().toString() + " - " + event.getEndDate().toString());
            eventTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            Button guestsButton = new Button("👥 Guests");
            Button settingsButton = new Button("⚙ Settings");
            Button statusButton = new Button("📊 Status");
            Button menuButton2 = new Button("⋮");

            String textButtonStyle2 = "-fx-background-color: transparent; -fx-border: none; -fx-font-size: 14px; -fx-text-fill: #8425A4;";
            guestsButton.setStyle(textButtonStyle2);
            settingsButton.setStyle(textButtonStyle2);
            statusButton.setStyle(textButtonStyle2);
            menuButton2.setStyle(textButtonStyle2);

            Region eventSpacer = new Region();
            HBox.setHgrow(eventSpacer, Priority.ALWAYS);

            eventBox.getChildren().addAll(eventTitle, eventSpacer, guestsButton, settingsButton, statusButton, menuButton2);

            StackPane eventWrapper = new StackPane(eventBox);
            eventWrapper.setMaxWidth(900);
            eventList.getChildren().add(eventWrapper);
        }
    }

    public VBox getComponent() {
        return eventList;
    }
}
