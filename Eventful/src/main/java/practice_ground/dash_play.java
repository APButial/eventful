package practice_ground;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class dash_play extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Left VBox (Sidebar) - 30% of screen width
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(40));
        sidebar.setStyle("-fx-background-color: #ECECEC; -fx-border-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        sidebar.setPrefWidth(300); // Adjusted for 30% width
        sidebar.setAlignment(Pos.TOP_CENTER);

        Label logo = new Label("Eventful");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button createEvent = new Button("Create Event");
        Button myEvents = new Button("My Events");
        Button eventTimeline = new Button("Event Timeline");
        Button logs = new Button("Logs");
        sidebar.getChildren().addAll(logo, createEvent, myEvents, eventTimeline, logs);

        // Right VBox (Main Content) - 70% of screen width
        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(40));
        mainContent.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
        mainContent.setPrefWidth(900); // Adjusted for 70% width

        // Upper Header Section
        HBox upperHeader = new HBox(20);
        upperHeader.setPadding(new Insets(10, 20, 10, 20));
        upperHeader.setAlignment(Pos.CENTER_RIGHT);
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search");
        searchBar.setPrefWidth(250);
        Button calendarButton = new Button("📅");
        Button notificationsButton = new Button("🔔");
        Button profileButton = new Button("👤");
        Button menuButton = new Button("⋮");

        upperHeader.getChildren().addAll(searchBar, calendarButton, notificationsButton, profileButton, menuButton);

        // Lower Header Section
        HBox lowerHeader = new HBox(20);
        lowerHeader.setPadding(new Insets(10, 20, 10, 20));
        lowerHeader.setAlignment(Pos.CENTER_LEFT);

        Label recentEventsLabel = new Label("Recent Events");
        recentEventsLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Label dateLabel = new Label("February 26, 2025 17:00");
        dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        HBox dayButtons = new HBox(5);
        for (String day : new String[]{"SU", "MO", "TU", "WE", "TH", "FR", "SA"}) {
            Button dayButton = new Button(day);
            dayButton.setStyle("-fx-background-color: #CCC; -fx-border-radius: 5;"); // Placeholder styling
            dayButtons.getChildren().add(dayButton);
        }

        lowerHeader.getChildren().addAll(recentEventsLabel, headerSpacer, dateLabel, dayButtons);

        // Event List
        VBox eventList = new VBox(10);
        eventList.setPadding(new Insets(10));

        for (int i = 0; i < 4; i++) {// increase the number para dumame ung events kayo na bala AHAHAHHA
            HBox event = new HBox(20);
            event.setPadding(new Insets(20));
            event.setAlignment(Pos.CENTER_LEFT);
            event.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");

            Label eventTitle = new Label("Event " );//date
            Label eventDate= new Label((  "\nJan " + (i + 5) + " 09:30" ));
            eventDate.setStyle("-fx-font-size:10px;");
            eventTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            Button guestsButton = new Button("👥 Guests");
            Button settingsButton = new Button("⚙ Settings");
            Button statusButton = new Button("📊 Status");
            Button menuButton2 = new Button("⋮");

            Region eventSpacer = new Region();
            HBox.setHgrow(eventSpacer, Priority.ALWAYS);

            event.getChildren().addAll(eventTitle,eventDate, eventSpacer, guestsButton, settingsButton, statusButton, menuButton2);
            eventList.getChildren().add(event);
        }

        mainContent.getChildren().addAll(upperHeader, lowerHeader, eventList);

        // Layout using HBox to split 30-70
        HBox layout = new HBox(sidebar, mainContent);
        layout.setSpacing(10);

        Scene scene = new Scene(layout, 1024, 600); // Increased scene size for better spacing
        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
