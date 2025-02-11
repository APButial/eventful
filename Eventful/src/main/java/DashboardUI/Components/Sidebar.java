package DashboardUI.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Sidebar {
    private VBox sidebar;

    public Sidebar(String activePage) {
        sidebar = new VBox(20);
        sidebar.setPadding(new Insets(40, 20, 40, 10)); // Adjust left padding for indicator space
        sidebar.setStyle("-fx-background-color: #F5F6FA; -fx-border-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        sidebar.setPrefWidth(400);
        sidebar.setAlignment(Pos.TOP_CENTER);

        HBox logo = new HBox();
        logo.setAlignment(Pos.CENTER);
        logo.setPadding(new Insets(0, 0, 20, 0));
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(120);
        logoView.setPreserveRatio(true);
        logo.getChildren().add(logoView);

        sidebar.getChildren().addAll(logo,
                createButton("/create.png", "Create Event", activePage.equals("Create Event")),
                createButton("/events.png", "My Events", activePage.equals("My Events")),
                createButton("/timeline.png", "Event Timeline", activePage.equals("Event Timeline")),
                createButton("/logs.png", "Logs", activePage.equals("Logs")));
    }

    private HBox createButton(String iconPath, String text, boolean isActive) {
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(16);
        iconView.setFitHeight(16);

        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-border: none; -fx-font-size: 14px; -fx-text-fill: " + (isActive ? "#6A0DAD" : "#AAB2C8") + ";");

        Rectangle indicator = new Rectangle(5, 30, Color.web("#6A0DAD"));
        indicator.setVisible(isActive); // Show only if active

        HBox buttonContainer = new HBox(10, iconView, button);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);

        HBox wrapper = new HBox(5, isActive ? indicator : new Region(), buttonContainer);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        return wrapper;
    }

    public VBox getComponent() {
        return sidebar;
    }
}
