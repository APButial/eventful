package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.service.CreateEventListener;
import com.btp.dashboard.service.DashNavigateListener;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CreateEventUI extends Application {
    private AppService appService;
    private DashNavigateListener dashNavigateListener;
    private CreateEventListener createEventListener;
    private EventDetails eventDetails;

    public CreateEventUI(AppService appService, DashNavigateListener dashNavigateListener, CreateEventListener createEventListener) {
        this.appService = appService;
        this.dashNavigateListener = dashNavigateListener;
        this.createEventListener = createEventListener;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create Event");

        Sidebar sidebar = new Sidebar("Create Event", primaryStage, appService, dashNavigateListener);//type the name of page for indicator
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader(appService, primaryStage);
        LowerHeader lowerHeader = new LowerHeader("Create Event", "no"); // "no" to hide date
        HBox spacer = new HBox();

        spacer.setPrefHeight(3);//spacer
        spacer.setStyle("-fx-background-color: #800080;");
        // Bottom Purple Bar
        HBox bottomBar = new HBox();
        bottomBar.setStyle("-fx-background-color: purple;");
        bottomBar.setMaxWidth(Double.MAX_VALUE); // Expand to full width
        bottomBar.setPrefHeight(70); // Fixed height
        HBox.setHgrow(bottomBar, Priority.ALWAYS); // Allow horizontal expansion
// Add a spacer to push bottomBar down
        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);
        eventDetails = new EventDetails(appService);

        eventDetails.getCancelButton().setOnAction(event -> {
            try {
                createEventListener.onCancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        eventDetails.getConfirmButton().setOnAction(event -> {
            createEventListener.onConfirm();
        });

// Add components in order, ensuring bottomSpacer is included
        mainContent.getChildren().addAll(
                upperHeader.getComponent(),
                lowerHeader.getComponent(),
                spacer,
                eventDetails.getComponent(),
                bottomSpacer, // Pushes bottomBar to the bottom
                bottomBar
        );

        HBox layout = new HBox(sidebar.getComponent(),mainContent);
        layout.setSpacing(0);

        appService.setDescription("");
        appService.setGuests(new ArrayList<>());

        Scene scene = new Scene(layout, 1024, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public EventDetails getEventDetails() {
        return eventDetails;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
