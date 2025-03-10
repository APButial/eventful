package com.btp.dashboard.component;

import com.btp.appfx.service.AppService;
import com.btp.dashboard.service.DashNavigateListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Sidebar {
    private VBox sidebar;
    private Stage stage;
    private AppService appService;
    private DashNavigateListener listener;

    public Sidebar(String activePage, Stage stage, AppService appService, DashNavigateListener listener) {
        this.stage = stage;
        this.listener = listener;
        this.appService = appService;

        sidebar = new VBox(20);
        sidebar.setPadding(new Insets(40, 20, 40, 0)); // Adjust left padding for indicator space
        sidebar.setStyle("-fx-background-color: #F5F6FA; -fx-border-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        sidebar.setPrefWidth(400);
        sidebar.setAlignment(Pos.TOP_CENTER);

        HBox logo = new HBox();
        logo.setAlignment(Pos.CENTER);
        logo.setPadding(new Insets(0, 0, 20, 0));
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoView = new ImageView(logoImage);

        Button logoButton = new Button();
        logoButton.setGraphic(logoView);
        logoButton.setBackground(null);

        ColorAdjust colorAdjust = new ColorAdjust();
        logoButton.setOnMousePressed(e -> {
            colorAdjust.setBrightness(-0.2);
            logoView.setEffect(colorAdjust);
        });
        logoButton.setOnMouseReleased(e -> {
            colorAdjust.setBrightness(0);
            logoView.setEffect(null);
        });
        logoButton.setOnAction(event -> {
            listener.logoTriggered();
        });

        logoView.setFitWidth(120);
        logoView.setPreserveRatio(true);
        logo.getChildren().add(logoButton);

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

        switch (text) {
            case "Create Event": {
                button.setOnAction(event -> {
                    listener.createEventTriggered();
                });
                break;
            }
            case "My Events": {
                button.setOnAction(event -> {
                    listener.myEventsTriggered();
                });
                break;
            }
            case "Event Timeline": {
                button.setOnAction(event -> {
                    listener.eventTimelineTriggered();
                });
                break;
            }
            case "Logs": {
                button.setOnAction(event -> {
                    listener.logsTriggered();
                });
                break;
            }
            default: {
                System.out.println("Error in sidebar");
                break;
            }
        }

        // HBox to ensure text stays left-aligned
        HBox textContainer = new HBox(button);
        textContainer.setAlignment(Pos.CENTER_LEFT);

        // HBox for icon and text, ensuring left alignment
        HBox buttonContent = new HBox(10, iconView, textContainer);
        buttonContent.setAlignment(Pos.CENTER_LEFT);
        buttonContent.setPadding(new Insets(0, 0, 0, 15)); // Adds space between indicator and icon

        // Indicator (far left)
        Rectangle indicator = new Rectangle(5, 30, Color.web("#6A0DAD"));
        indicator.setVisible(isActive); // Only show if active

        // Wrapper keeps indicator at the far left while keeping content aligned
        HBox wrapper = new HBox(indicator, buttonContent);
        wrapper.setAlignment(Pos.CENTER_LEFT); // Keeps content aligned left while keeping the structure
        return wrapper;
    }


    public VBox getComponent() {
        return sidebar;
    }
}
