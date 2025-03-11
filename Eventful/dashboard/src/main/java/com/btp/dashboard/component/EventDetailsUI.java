package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.service.DashNavigateListener;
import com.btp.dashboard.service.EventDetailListener;
import com.btp.dashboard.service.EventFormListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

public class EventDetailsUI extends Application {
    private AppService appService;
    private DashNavigateListener dashNavigateListener;
    private EventDetailListener eventDetailListener;
    private EventFormListener eventFormListener;
    private EventForm eventForm;

    public EventDetailsUI(AppService appService, DashNavigateListener dashNavigateListener, EventDetailListener eventDetailListener, EventFormListener eventFormListener) {
        this.appService = appService;
        this.dashNavigateListener = dashNavigateListener;
        this.eventDetailListener = eventDetailListener;
        this.eventFormListener = eventFormListener;
        this.eventForm = new EventForm(eventFormListener);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Event Details");

        Sidebar sidebar = new Sidebar("My Events", primaryStage, appService, dashNavigateListener);
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader(appService, dashNavigateListener);
        LowerHeader lowerHeader = new LowerHeader("Event Details - " + appService.getEventName().toUpperCase(), "no"); // "no" to hide date
        HBox spacer = new HBox();

        spacer.setPrefHeight(3);
        spacer.setStyle("-fx-background-color: #800080;");

        // Bottom Purple Bar
        HBox bottomBar = new HBox();
        bottomBar.setStyle("-fx-background-color: purple;");
        bottomBar.setMaxWidth(Double.MAX_VALUE);
        bottomBar.setPrefHeight(70);
        HBox.setHgrow(bottomBar, Priority.ALWAYS);

        // Add a spacer to push bottomBar down
        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        // Add components in order, ensuring bottomSpacer is included
        mainContent.getChildren().addAll(
                upperHeader.getComponent(),
                lowerHeader.getComponent(),
                spacer,
                eventForm.getComponent() // Ensure this method exists in EventForm

        );

        HBox layout = new HBox(sidebar.getComponent(), mainContent);
        layout.setSpacing(0);
        HBox.setHgrow(mainContent, Priority.ALWAYS); // Ensure mainContent expands properly

        Scene scene = new Scene(layout, 1024, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        eventDetailListener.onOpen();
    }

    public EventForm getEventForm() {
        return eventForm;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
