package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.service.DashNavigateListener;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LogsUI extends Application {
    private AppService appService;
    private DashNavigateListener dashNavigateListener;
    private LogsArea logsArea;

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

        UpperHeader upperHeader = new UpperHeader(appService, dashNavigateListener);
        LowerHeader lowerHeader = new LowerHeader("Logs", "no"); // Show date

        HBox spacer = new HBox();
        spacer.setPrefHeight(3);
        spacer.setStyle("-fx-background-color: #800080;");

        logsArea = new LogsArea();

        mainContent.getChildren().addAll(upperHeader.getComponent(), lowerHeader.getComponent(), spacer, logsArea.getComponent());

        HBox layout = new HBox(sidebar.getComponent(), mainContent);
        layout.setSpacing(0);

        Scene scene = new Scene(layout, 1024, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public LogsArea getLogsArea() {
        return logsArea;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
