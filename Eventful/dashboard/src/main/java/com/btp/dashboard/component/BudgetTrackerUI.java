package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.component.BudgetTable;
import com.btp.dashboard.component.Sidebar;
import com.btp.dashboard.component.UpperHeader;
import com.btp.dashboard.component.LowerHeader;
import com.btp.dashboard.service.DashNavigateListener;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BudgetTrackerUI extends Application {
    private AppService appService;
    private DashNavigateListener dashNavigateListener;

    public BudgetTrackerUI(AppService appService, DashNavigateListener dashNavigateListener) {
        this.appService = appService;
        this.dashNavigateListener = dashNavigateListener;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Budget Tracker");

        Sidebar sidebar = new Sidebar("My Events", primaryStage, appService, dashNavigateListener);
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader();
        LowerHeader lowerHeader = new LowerHeader("Budget - " + appService.getSelectedEvent().getEventName(), "no");
        HBox spacer = new HBox();
        spacer.setPrefHeight(3);
        spacer.setStyle("-fx-background-color: #800080;");

        BudgetTable budgetTable = new BudgetTable();

        mainContent.getChildren().addAll(upperHeader.getComponent(), lowerHeader.getComponent(), spacer, budgetTable.getComponent());

        HBox layout = new HBox(sidebar.getComponent(), mainContent);
        layout.setSpacing(0);
//        Image logoImg = new Image(getClass().getResourceAsStream("/logo.png"));
//        ImageView logoView = new ImageView(logoImg);
//        primaryStage.getIcons().add(logoImg);

        Scene scene = new Scene(layout, 1024, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
