package com.btp.event_manager.component;

import com.btp.appfx.service.AppService;
import com.btp.event_manager.service.DashNavigateListener;
import com.btp.event_manager.service.DateTimeListener;
import com.btp.event_manager.service.EventDetailListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;

public class MyEventUI extends Application {
    private AppService appService;
    private DashNavigateListener listener;
    private EventDetailListener eventDetailListener;

    public MyEventUI(AppService appService, DashNavigateListener listener, EventDetailListener eventDetailListener) {
        this.appService = appService;
        this.listener = listener;
        this.eventDetailListener = eventDetailListener;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("My Events");

        Sidebar sidebar = new Sidebar("My Events", primaryStage, appService, listener); // Highlight My Events in sidebar
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader(appService, primaryStage);
        LowerHeader lowerHeader = new LowerHeader("My Events", "yes"); // Show date
        lowerHeader.setDateLabel(appService.getSysDateTime());
        lowerHeader.setDateTimeListener(new DateTimeListener() {
            @Override
            public void updateDateTime(LocalDateTime localDateTime) {
                lowerHeader.setDateLabel(localDateTime);
            }
        });
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            lowerHeader.setDateLabel(appService.getSysDateTime());
            lowerHeader.getDayButtons().getChildren().get(appService.getSysDateTime().getDayOfWeek().getValue() % 7).setStyle("-fx-background-color: #8425A4; -fx-text-fill: #FFFFFF");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        HBox spacer = new HBox();
        spacer.setPrefHeight(3);
        spacer.setStyle("-fx-background-color: #800080;");

        MyEvent myEvent = new MyEvent(appService, eventDetailListener);

        mainContent.getChildren().addAll(upperHeader.getComponent(), lowerHeader.getComponent(), spacer, myEvent.getComponent());

        HBox layout = new HBox(sidebar.getComponent(), mainContent);
        layout.setSpacing(0);

        Scene scene = new Scene(layout, 1024, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}