package com.btp.event_manager.component;

import com.btp.event_manager.service.DateTimeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LowerHeader {
    private HBox lowerHeader;
    private HBox dayButtons;
    private Label dateLabel;
    private DateTimeListener dateTimeListener;

    public LowerHeader(String headerText, String showDate) { // 'showDate' controls visibility
        lowerHeader = new HBox(20);
        lowerHeader.setPadding(new Insets(10, 20, 10, 20));
        lowerHeader.setAlignment(Pos.CENTER_LEFT);

        Label headerLabel = new Label(headerText);
        headerLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        lowerHeader.getChildren().addAll(headerLabel, headerSpacer);

        if (showDate.equalsIgnoreCase("yes")) { // Check if date section should be included
            dateLabel = new Label();
            dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            dayButtons = new HBox(5);
            dayButtons.setAlignment(Pos.CENTER_LEFT);
            for (String day : new String[]{"SU", "MO", "TU", "WE", "TH", "FR", "SA"}) {
                Button dayButton = new Button(day);
                dayButton.setStyle("-fx-background-color: #555555; -fx-border-radius: 5; -fx-text-fill: #FFFFFF;");
                dayButtons.getChildren().add(dayButton);
            }

            VBox dateAndDays = new VBox(5, dateLabel, dayButtons); // Date & buttons stacked vertically
            lowerHeader.getChildren().add(dateAndDays);
        }
    }

    public HBox getComponent() {
        return lowerHeader;
    }

    public Label getDateLabel() {
        return dateLabel;
    }

    public void setDateLabel(LocalDateTime date) {
        StringBuilder dateStringBuilder = new StringBuilder();

        String month = date.getMonth().name().toLowerCase();
        month = Character.toUpperCase(month.charAt(0)) + month.substring(1);
        dateStringBuilder.append(month).append(" ");

        dateStringBuilder.append(date.getDayOfMonth()).append(", ");
        dateStringBuilder.append(date.getYear()).append(" ");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        dateStringBuilder.append(date.format(timeFormatter));

        dateLabel.setText(dateStringBuilder.toString());
    }

    public HBox getLowerHeader() {
        return lowerHeader;
    }

    public void setDateTimeListener(DateTimeListener listener) {
        this.dateTimeListener = listener;
    }

    public HBox getDayButtons() {
        return dayButtons;
    }
}
