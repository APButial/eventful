package com.btp.event_manager.component;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class BackupPopup extends Application {

    @Override
    public void start(Stage primaryStage) {
        showBackupSuccessPopup("Backup has been successfully created.", primaryStage);
    }

    private void showBackupSuccessPopup(String message, Stage primaryStage) {
        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);

        Label messageLabel = new Label(message);
        Button closeButton = new Button("x");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: purple; -fx-font-size: 14px;");
        closeButton.setOnAction(event -> popupStage.close());

        AnchorPane popupLayout = new AnchorPane();
        popupLayout.setStyle("-fx-background-color: white; -fx-padding: 10;-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 8, 0.5, 0, 2);");
        popupLayout.getChildren().addAll(messageLabel, closeButton);

        AnchorPane.setTopAnchor(closeButton, 2.0);
        AnchorPane.setRightAnchor(closeButton, 2.0);
        AnchorPane.setLeftAnchor(messageLabel, 15.0);
        AnchorPane.setTopAnchor(messageLabel, 15.0);

        Scene popupScene = new Scene(popupLayout, 300, 80); // set fixed size
        popupStage.setScene(popupScene);
        popupStage.setAlwaysOnTop(true);

        // Show the popup first to get its actual dimensions
        popupStage.show();

        // Use Platform.runLater to wait until layout is computed
        javafx.application.Platform.runLater(() -> {
            double popupWidth = popupStage.getWidth();
            double popupHeight = popupStage.getHeight();

            double x = primaryStage.getX() + primaryStage.getWidth() - popupWidth - 15;
            double y = primaryStage.getY() + primaryStage.getHeight() - popupHeight - 15;

            popupStage.setX(x);
            popupStage.setY(y);
        });

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            if (popupStage.isShowing()) {
                popupStage.close();
            }
        });
        pause.play();
    }
}