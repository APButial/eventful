package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.service.DashNavigateListener;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BudgetTrackerUI extends Application {
    private AppService appService;
    private DashNavigateListener dashNavigateListener;
    private BudgetTable budgetTable;

    public BudgetTrackerUI(AppService appService, DashNavigateListener dashNavigateListener) {
        this.appService = appService;
        this.dashNavigateListener = dashNavigateListener;
        budgetTable = new BudgetTable();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expenses Tracker");

        Sidebar sidebar = new Sidebar("My Events", primaryStage, appService, dashNavigateListener);
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader(appService, primaryStage);
        LowerHeader lowerHeader = new LowerHeader("Expenses Tracker - " + appService.getSelectedEvent().getEventName().toUpperCase(), "no");

        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-background-color: #8425a4; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5px;");
        returnButton.setOnAction(event -> {
            dashNavigateListener.returnTriggered();
        });
        lowerHeader.getLowerHeader().getChildren().add(returnButton);

        Button updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color: #8425a4; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5px;");
        updateButton.setOnAction(event -> {
            dashNavigateListener.budgetUpdateTriggered();
        });
        lowerHeader.getLowerHeader().getChildren().add(updateButton);


        HBox spacer = new HBox();
        spacer.setPrefHeight(3);
        spacer.setStyle("-fx-background-color: #800080;");

        ScrollPane scrollPane = new ScrollPane(budgetTable.getComponent());
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        mainContent.getChildren().addAll(upperHeader.getComponent(), lowerHeader.getComponent(), spacer, scrollPane);

        HBox layout = new HBox(sidebar.getComponent(), mainContent);
        layout.setSpacing(0);

        Scene scene = new Scene(layout, 1024, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public BudgetTable getBudgetTable() {
        return budgetTable;
    }

    public void clearTable () {
        budgetTable = new BudgetTable();
    }
}
