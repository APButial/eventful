package DashboardUI.Components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EventDetails {
    private VBox component;

    public EventDetails() {
        component = new VBox(10);
        component.setPadding(new Insets(20, 40, 40, 40));
        component.setStyle("-fx-background-color: #FFFFFF;");



        // Form Fields
        GridPane form = new GridPane();
        form.setVgap(15);
        form.setHgap(10);

        Label eventNameLabel = new Label("Event Name");
        TextField eventNameField = new TextField();
        eventNameField.setPromptText("Enter event name");
        eventNameField.setPrefWidth(400);

        Label startDateLabel = new Label("Start Date");
        TextField startDateField = new TextField();
        startDateField.setPromptText("Enter event start date");

        Label endDateLabel = new Label("End Date");
        TextField endDateField = new TextField();
        endDateField.setPromptText("Enter event end date");

        form.add(eventNameLabel, 0, 0);
        form.add(eventNameField, 0, 1);
        form.add(startDateLabel, 0, 2);
        form.add(startDateField, 0, 3);
        form.add(endDateLabel, 0, 4);
        form.add(endDateField, 0, 5);

        // Buttons
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: transparent; -fx-border-color: purple; -fx-text-fill: purple;");

        Button confirmButton = new Button("Confirm");
        confirmButton.setStyle("-fx-background-color: purple; -fx-text-fill: white;");

        HBox buttonBox = new HBox(20, cancelButton, confirmButton);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        component.getChildren().addAll( form, buttonBox);
    }

    public VBox getComponent() {
        return component;
    }
}
