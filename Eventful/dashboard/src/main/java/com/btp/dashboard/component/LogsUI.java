package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.service.DashNavigateListener;
import com.btp.dashboard.service.DateTimeListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;

public class LogsUI extends Application {
    private AppService appService;
    private DashNavigateListener dashNavigateListener;

    public LogsUI(AppService appService, DashNavigateListener dashNavigateListener) {
        this.appService = appService;
        this.dashNavigateListener = dashNavigateListener;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("My Events");

        Sidebar sidebar = new Sidebar("Logs", primaryStage, appService, dashNavigateListener); // Highlight My Events in sidebar
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader();
        LowerHeader lowerHeader = new LowerHeader("Logs", "no"); // Show date

        HBox spacer = new HBox();
        spacer.setPrefHeight(3);
        spacer.setStyle("-fx-background-color: #800080;");

        LogsArea logsArea = new LogsArea();

        mainContent.getChildren().addAll(upperHeader.getComponent(), lowerHeader.getComponent(), spacer, logsArea.getComponent());

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
