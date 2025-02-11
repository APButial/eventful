package DashboardUI.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class LowerHeader {
    private HBox lowerHeader;

    public LowerHeader() {
        lowerHeader = new HBox(20);
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
    }

    public HBox getComponent() {
        return lowerHeader;
    }
}
