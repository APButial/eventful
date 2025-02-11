package DashboardUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(40));
        sidebar.setStyle("-fx-background-color: #F5F6FA; -fx-border-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        sidebar.setPrefWidth(300);
        sidebar.setAlignment(Pos.TOP_CENTER);

// Logo using HBox and ImageView
        HBox logo = new HBox();
        logo.setAlignment(Pos.CENTER);
        logo.setPadding(new Insets(0, 0, 20, 0)); // Adjusted padding for spacing
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(120);
        logoView.setPreserveRatio(true);
        logo.getChildren().add(logoView);

// Create buttons without button-like styling
        Button createEvent = new Button("Create Event");
        Button myEvents = new Button("My Events");
        Button eventTimeline = new Button("Event Timeline");
        Button logs = new Button("Logs");

        // just some styles so it looks like text but still functions as the labels like in our wireframe
        String textButtonStyle = "-fx-background-color: transparent; -fx-border: none; -fx-font-size: 14px; -fx-text-fill: #AAB2C8;";
        createEvent.setStyle(textButtonStyle);
        myEvents.setStyle(textButtonStyle);
        eventTimeline.setStyle(textButtonStyle);
        logs.setStyle(textButtonStyle);
        //add da children else nothing will show
        sidebar.getChildren().addAll(logo, createEvent, myEvents, eventTimeline, logs);

        //  70% of screen width change if everr
        VBox mainContent = new VBox(0);
        mainContent.setPadding(new Insets(0));
        mainContent.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
        mainContent.setPrefWidth(900);

        // Upper Head Section
        HBox upperHeader = new HBox(10);
        upperHeader.setPadding(new Insets(5, 20, 5, 20));
        upperHeader.setAlignment(Pos.TOP_RIGHT);
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search");
        searchBar.setPrefWidth(250);
        Button calendarButton = new Button("📅");
        Button notificationsButton = new Button("🔔");
        Button profileButton = new Button("👤");
        Button menuButton = new Button("⋮");
        //same styling as the sidebar just call textButtonStyle
        calendarButton.setStyle(textButtonStyle);
        notificationsButton.setStyle(textButtonStyle);
        profileButton.setStyle(textButtonStyle);
        menuButton.setStyle(textButtonStyle);

        HBox profileMenu = new HBox(profileButton, menuButton);
        profileMenu.setSpacing(0);

        upperHeader.getChildren().addAll(searchBar, calendarButton, notificationsButton, profileMenu);

        // Lower jeader Section
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
            dayButton.setStyle("-fx-background-color: #CCC; -fx-border-radius: 5;");
            dayButtons.getChildren().add(dayButton);
        }

        lowerHeader.getChildren().addAll(recentEventsLabel, headerSpacer, dateLabel, dayButtons);
        //just the purple line on the pane
        HBox separator = new HBox();
        separator.setPrefHeight(2); // Thin line
        separator.setStyle("-fx-background-color: #800080;"); // Purple color

        // Event List
        VBox eventList = new VBox(10);
        eventList.setPadding(new Insets(10, 20, 10, 20));

        for (int i = 0; i < 5; i++) {// just to simulate multiple events
            HBox event = new HBox(20);
            event.setPadding(new Insets(15));
            event.setAlignment(Pos.CENTER_LEFT);
            event.setStyle("-fx-background-color: #F5F6FA; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
            // Make event width slightly smaller
            event.setMaxWidth(900);
            Label eventTitle = new Label("Event " + (i + 1) + "\nJan " + (i + 5) + " 09:30");
            eventTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            Button guestsButton = new Button("👥 Guests");
            Button settingsButton = new Button("⚙ Settings");
            Button statusButton = new Button("📊 Status");
            Button menuButton2 = new Button("⋮");//maybe make another one for this idk

            String textButtonStyle2 = "-fx-background-color: transparent; -fx-border: none; -fx-font-size: 14px; -fx-text-fill: #8425A4;";
            guestsButton.setStyle(textButtonStyle2);
            settingsButton.setStyle(textButtonStyle2);
            statusButton.setStyle(textButtonStyle2);
            menuButton2.setStyle(textButtonStyle2);

            Region eventSpacer = new Region();
            HBox.setHgrow(eventSpacer, Priority.ALWAYS);
            event.getChildren().addAll(eventTitle, eventSpacer, guestsButton, settingsButton, statusButton, menuButton2);

            //just for design
            StackPane eventWrapper = new StackPane(event);
            eventWrapper.setMaxWidth(900); // Slightly smaller than VBox width
            eventList.getChildren().add(eventWrapper);
        }
        mainContent.getChildren().addAll(upperHeader, lowerHeader,separator, eventList);//loads the boxes/panes in order

        HBox layout = new HBox(sidebar, mainContent);
        layout.setSpacing(0);

        Scene scene = new Scene(layout, 1024, 600);
        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
