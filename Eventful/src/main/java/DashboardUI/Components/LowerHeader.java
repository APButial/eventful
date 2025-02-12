package DashboardUI.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LowerHeader {
    private HBox lowerHeader;

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
            Label dateLabel = new Label("February 26, 2025 17:00");
            dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            HBox dayButtons = new HBox(5);
            dayButtons.setAlignment(Pos.CENTER_LEFT);
            for (String day : new String[]{"SU", "MO", "TU", "WE", "TH", "FR", "SA"}) {
                Button dayButton = new Button(day);
                dayButton.setStyle("-fx-background-color: #CCC; -fx-border-radius: 5;");
                dayButtons.getChildren().add(dayButton);
            }

            VBox dateAndDays = new VBox(5, dateLabel, dayButtons); // Date & buttons stacked vertically
            lowerHeader.getChildren().add(dateAndDays);
        }
    }

    public HBox getComponent() {
        return lowerHeader;
    }
}
