package com.btp.component;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dashboard");

        Sidebar sidebar = new Sidebar("none");//type the name of which page being used for indicator
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader();
        LowerHeader lowerHeader = new LowerHeader("Recent Events","yes");//requires string for page name
        HBox spacer = new HBox();                           // type 'yes' to show date or 'no' if date is not included

        spacer.setPrefHeight(3);
        spacer.setStyle("-fx-background-color: #800080;");

        EventList eventList = new EventList();

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
