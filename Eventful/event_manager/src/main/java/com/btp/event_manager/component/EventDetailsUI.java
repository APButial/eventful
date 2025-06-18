package com.btp.event_manager.component;

import com.btp.appfx.service.AppService;
import com.btp.event_manager.service.DashNavigateListener;
import com.btp.event_manager.service.EventDetailListener;
import com.btp.event_manager.service.EventFormListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

import java.io.IOException;

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
        this.eventForm = new EventForm(appService, eventFormListener);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Event Details");

        Sidebar sidebar = new Sidebar("My Events", primaryStage, appService, dashNavigateListener);
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader(appService, primaryStage);
        LowerHeader lowerHeader = new LowerHeader("Event Details - " + appService.getEventName().toUpperCase(), "no"); // "no" to hide date

        Button inboxButton = new Button();
        ImageView inboxImg = new ImageView("/inbox.png");
        inboxButton.setGraphic(inboxImg);
        inboxButton.setBackground(null);
        inboxButton.setPrefWidth(15);
        inboxButton.setPrefHeight(15);
        inboxButton.setOnAction(event -> {
            eventFormListener.onInbox();
        });

        ColorAdjust colorAdjust = new ColorAdjust();
        inboxButton.setOnMousePressed(e -> {
            colorAdjust.setBrightness(-0.2);
            inboxImg.setEffect(colorAdjust);
        });
        inboxButton.setOnMouseReleased(e -> {
            colorAdjust.setBrightness(0);
            inboxImg.setEffect(null);
        });

        Button exportButton = new Button();
        ImageView exportImg = new ImageView("/export.png");
        exportButton.setGraphic(exportImg);
        exportButton.setBackground(null);
        exportButton.setPrefWidth(80);
        exportButton.setPrefHeight(15);
        exportButton.setOnAction(event -> {
            try {
                eventFormListener.onExport();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        exportButton.setOnMousePressed(e -> {
            colorAdjust.setBrightness(-0.2);
            exportImg.setEffect(colorAdjust);
        });
        exportButton.setOnMouseReleased(e -> {
            colorAdjust.setBrightness(0);
            exportImg.setEffect(null);
        });

        lowerHeader.getComponent().getChildren().add(inboxButton);
        lowerHeader.getComponent().getChildren().add(exportButton);

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
