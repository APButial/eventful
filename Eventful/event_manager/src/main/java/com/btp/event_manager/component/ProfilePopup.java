package com.btp.event_manager.component;

import com.btp.appfx.service.AppService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;

public class ProfilePopup extends Popup {
    public ProfilePopup(AppService appService) {
        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER);
        userBox.setPrefHeight(50);
        Circle profileIcon = new Circle(15);
        profileIcon.setFill(new ImagePattern(new Image("/profile.png")));
        Label username = new Label(appService.getCurrUser().getUsername());
        userBox.getChildren().addAll(profileIcon, username);

        VBox buttonBox = new VBox(5);
        Button delAccountButton = new Button("Delete Account");
        delAccountButton.setBackground(null);
        delAccountButton.setStyle("-fx-font-size: 12");
        delAccountButton.setOnAction(event -> {
            appService.deleteAccount();
        });
        delAccountButton.setOnMouseEntered(mouseEvent -> {
            delAccountButton.setStyle("-fx-text-fill: #8425A4; -fx-font-size: 12"); // Change text color on mouse enter
        });
        delAccountButton.setOnMouseExited(mouseEvent -> {
            delAccountButton.setStyle("-fx-text-fill: black; -fx-font-size: 12"); // Reset text color on mouse exit
        });
        delAccountButton.setOnAction(event -> {
            appService.deleteAccount();
            hide();
        });


        Button logoutButton = new Button("Logout");
        logoutButton.setBackground(null);
        logoutButton.setStyle("-fx-font-size: 12");
        logoutButton.setOnAction(event -> {
            appService.logout();
            hide();
        });
        logoutButton.setOnMouseEntered(mouseEvent -> {
            logoutButton.setStyle("-fx-text-fill: #8425A4; -fx-font-size: 12"); // Change text color on mouse enter
        });

        logoutButton.setOnMouseExited(mouseEvent -> {
            logoutButton.setStyle("-fx-text-fill: black; -fx-font-size: 12"); // Reset text color on mouse exit
        });


        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().addAll(delAccountButton, logoutButton);

        VBox content = new VBox(5);
        content.getChildren().addAll(userBox, buttonBox);
        content.setBackground(new Background(new BackgroundFill(Color.web("#F5F6FA"), new CornerRadii(15), Insets.EMPTY)));
        content.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(1))));

        content.setPadding(new Insets(10,10,10,10));
        content.setPrefSize(150,80);

        getContent().add(content);
        setAutoHide(true);

    }
}
