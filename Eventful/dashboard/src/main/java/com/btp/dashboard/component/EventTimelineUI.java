package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.service.DashNavigateListener;
import com.btp.dashboard.service.DateTimeListener;
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

public class EventTimelineUI extends Application {
    private AppService appService;
    private DashNavigateListener dashNavigateListener;
    private EventTimeline eventTimeline;

    public EventTimelineUI(AppService appService, DashNavigateListener dashNavigateListener) {
        this.appService = appService;
        this.dashNavigateListener = dashNavigateListener;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Event Timeline");

        Sidebar sidebar = new Sidebar("Event Timeline", stage, appService, dashNavigateListener);
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader();
        LowerHeader lowerHeader = new LowerHeader("Event Timeline", "yes"); // Show date
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

        eventTimeline = new EventTimeline();

        mainContent.getChildren().addAll(upperHeader.getComponent(), lowerHeader.getComponent(), spacer, eventTimeline.getComponent());

        HBox layout = new HBox(sidebar.getComponent(), mainContent);
        layout.setSpacing(0);

        Scene scene = new Scene(layout, 1024, 600);
        stage.setScene(scene);
        stage.show();
    }

    public EventTimeline getEventTimeline() {
        return eventTimeline;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
