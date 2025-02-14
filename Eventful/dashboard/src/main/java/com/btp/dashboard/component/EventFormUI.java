package DashboardUI.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.time.LocalDate;

public class EventFormUI {
    public Node getComponent() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        // Start Date
        Label startDateLabel = new Label("Start Date*");
        DatePicker startDatePicker = new DatePicker(LocalDate.now());
        startDatePicker.setPrefWidth(240);

        startDatePicker.setStyle("-fx-background-color: #F5F5F5; " +
                "-fx-border-color: transparent; " +
                "-fx-padding: 5px; " +
                "-fx-border-radius: 5px; " +
                "-fx-control-inner-background: #F5F5F5;" );


        HBox startDateBox = new HBox(5, startDatePicker);

        // End Date
        Label endDateLabel = new Label("End Date*");
        DatePicker endDatePicker = new DatePicker(LocalDate.now().plusDays(1));
        endDatePicker.setPrefWidth(240);

        endDatePicker.setStyle("-fx-background-color: #F5F5F5; " +
                "-fx-border-color: transparent; " +
                "-fx-padding: 5px; " +
                "-fx-border-radius: 5px; " +
                "-fx-control-inner-background: #F5F5F5;" );

        HBox endDateBox = new HBox(5, endDatePicker);

        // Time Start
        Label timeStartLabel = new Label("Time Start");
        TextField timeStartField = new TextField();
        timeStartField.setPromptText("Enter start time (e.g., 12:00 PM)");
        timeStartField.setPrefWidth(210);
        timeStartField.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: transparent; -fx-padding: 5px; -fx-border-radius: 5px;");
        ImageView timeStartIcon = new ImageView(new Image("/time.png"));
        timeStartIcon.setFitWidth(30);
        timeStartIcon.setPreserveRatio(true);
        HBox timeStartBox = new HBox(5, timeStartField, timeStartIcon);

        // Time End
        Label timeEndLabel = new Label("Time End");
        TextField timeEndField = new TextField();
        timeEndField.setPromptText("Enter end time (e.g., 3:00 PM)");
        timeEndField.setPrefWidth(210);
        timeEndField.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: transparent; -fx-padding: 5px; -fx-border-radius: 5px;");
        ImageView timeEndIcon = new ImageView(new Image("/time.png"));
        timeEndIcon.setFitWidth(30);
        timeEndIcon.setPreserveRatio(true);
        HBox timeEndBox = new HBox(5, timeEndField, timeEndIcon);

        // Event Description
        Label eventDescLabel = new Label("Event Description");
        TextArea eventDescArea = new TextArea();
        eventDescArea.setPrefWidth(400);
        eventDescArea.setPrefHeight(100);
        eventDescArea.setPromptText("Enter Event Description (Optional)");
        eventDescArea.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-control-inner-background: #F5F5F5;");

        // Left Section
        VBox leftSection = new VBox(10, startDateLabel, startDateBox, endDateLabel, endDateBox, timeStartLabel, timeStartBox, timeEndLabel, timeEndBox, eventDescLabel, eventDescArea);

        // Right Section
        Label guestsLabel = new Label("No. of Guests");
        TextField guestsField = new TextField("0");
        guestsField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-control-inner-background: #F5F5F5;");
        Button viewSeatsButton = new Button("View seat assignment");

        viewSeatsButton.setStyle("-fx-font-size: 10px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: blue; -fx-border-radius: 5px; ");
        HBox viewSeatsBox = new HBox(viewSeatsButton);
        viewSeatsBox.setAlignment(Pos.TOP_RIGHT);

        Label guestEmailsLabel = new Label("Guests");
        TextArea guestEmailsArea = new TextArea();
        guestEmailsArea.setPromptText("Enter guests’ email addresses (Optional)");
        guestEmailsArea.setPrefHeight(165);
        guestEmailsArea.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-control-inner-background: #F5F5F5;");

        Button sendEmailButton = new Button("Send Email Invitation");
        sendEmailButton.setStyle("-fx-background-color: transparent; -fx-border-color: purple; -fx-text-fill: purple; -fx-border-radius: 5px; -fx-padding: 10px 20px;");
        sendEmailButton.setPrefWidth(200);

        Button configureLayoutButton = new Button("Configure Layout");
        configureLayoutButton.setStyle("-fx-background-color: transparent; -fx-border-color: purple; -fx-text-fill: purple; -fx-border-radius: 5px; -fx-padding: 10px 20px;");
        configureLayoutButton.setPrefWidth(200);

        Button expensesTrackerButton = new Button("Expenses Tracker");
        expensesTrackerButton.setStyle("-fx-background-color: transparent; -fx-border-color: purple; -fx-text-fill: purple; -fx-border-radius: 5px; -fx-padding: 10px 20px;");
        expensesTrackerButton.setPrefWidth(200);


        VBox buttonBox = new VBox(10, sendEmailButton, configureLayoutButton, expensesTrackerButton);
        buttonBox.setPrefHeight(100);
        buttonBox.setAlignment(Pos.CENTER);


        VBox rightSection = new VBox(10, guestsLabel, guestsField, viewSeatsButton,viewSeatsBox, guestEmailsLabel, guestEmailsArea, buttonBox);

        // Main Container
        HBox formContainer = new HBox(20, leftSection, rightSection);
        formContainer.setPadding(new Insets(20, 30, 40, 30));
        formContainer.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 10px; -fx-padding: 20px; -fx-border-color: #CCCCCC; -fx-border-width: 1px;");

        return formContainer; // Return as Node
    }
}
