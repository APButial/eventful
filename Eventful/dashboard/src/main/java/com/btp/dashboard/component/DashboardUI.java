package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.service.DashNavigateListener;
import com.btp.dashboard.service.DateTimeListener;
import com.btp.dashboard.service.EventDetailListener;
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

public class DashboardUI extends Application {
    private AppService appService;
    private DashNavigateListener listener;
    private EventDetailListener eventDetailListener;

    public DashboardUI(AppService appService, DashNavigateListener listener, EventDetailListener eventDetailListener) {
        this.appService = appService;
        this.listener = listener;
        this.eventDetailListener = eventDetailListener;
    }

    @Override
    public void start(Stage primaryStage) {
        appService.setPrevApplication(this);
        primaryStage.setTitle("Dashboard");

        Sidebar sidebar = new Sidebar("None", primaryStage, appService, listener);//type the name of which page being used for indicator
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader();
        LowerHeader lowerHeader = new LowerHeader("Recent Events","yes");//requires string for page name
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

        HBox spacer = new HBox();                           // type 'yes' to show date or 'no' if date is not included

        spacer.setPrefHeight(3);
        spacer.setStyle("-fx-background-color: #800080;");

        EventList eventList = new EventList(appService, eventDetailListener);

        mainContent.getChildren().addAll(upperHeader.getComponent(), lowerHeader.getComponent(),spacer, eventList.getComponent());

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
