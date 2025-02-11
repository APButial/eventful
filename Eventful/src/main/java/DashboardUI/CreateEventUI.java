package DashboardUI;

import DashboardUI.Components.EventDetails;
import DashboardUI.Components.LowerHeader;
import DashboardUI.Components.Sidebar;
import DashboardUI.Components.UpperHeader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateEventUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create Event");

        Sidebar sidebar = new Sidebar("Create Event");//type the name of page for indicator
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
        mainContent.setPrefWidth(900);

        UpperHeader upperHeader = new UpperHeader();
        LowerHeader lowerHeader = new LowerHeader("Create Event", "no"); // "no" to hide date
        HBox spacer = new HBox();
        spacer.setPrefHeight(3);
        spacer.setStyle("-fx-background-color: #800080;");

        EventDetails eventDetailsConfirmation = new EventDetails();

        mainContent.getChildren().addAll(upperHeader.getComponent(), lowerHeader.getComponent(), spacer, eventDetailsConfirmation.getComponent());

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
