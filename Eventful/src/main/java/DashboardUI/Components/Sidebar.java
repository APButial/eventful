package DashboardUI.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Sidebar {
    private VBox sidebar;

    public Sidebar() {
        sidebar = new VBox(20);
        sidebar.setPadding(new Insets(40));
        sidebar.setStyle("-fx-background-color: #F5F6FA; -fx-border-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        sidebar.setPrefWidth(300);
        sidebar.setAlignment(Pos.TOP_CENTER);

        HBox logo = new HBox();
        logo.setAlignment(Pos.CENTER);
        logo.setPadding(new Insets(0, 0, 20, 0));
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(120);
        logoView.setPreserveRatio(true);
        logo.getChildren().add(logoView);

        Button createEvent = new Button("Create Event");
        Button myEvents = new Button("My Events");
        Button eventTimeline = new Button("Event Timeline");
        Button logs = new Button("Logs");

        String textButtonStyle = "-fx-background-color: transparent; -fx-border: none; -fx-font-size: 14px; -fx-text-fill: #AAB2C8;";
        createEvent.setStyle(textButtonStyle);
        myEvents.setStyle(textButtonStyle);
        eventTimeline.setStyle(textButtonStyle);
        logs.setStyle(textButtonStyle);

        sidebar.getChildren().addAll(logo, createEvent, myEvents, eventTimeline, logs);
    }

    public VBox getComponent() {
        return sidebar;
    }
}
