package com.btp.component;

import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class Logs {
    private VBox component;
    private TextArea logArea;

    public Logs() {
        component = new VBox();
        component.setPadding(new Insets(15));

        logArea = new TextArea();
        logArea.setWrapText(true);

        logArea.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 10px; " +
                        "-fx-control-inner-background: #F8F8FA; " +
                        "-fx-background-color: #F8F8FA; "

        );
        logArea.setPrefHeight(500);

        ScrollPane scrollPane = new ScrollPane(logArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        component.getChildren().add(scrollPane);
    }

    public VBox getComponent() {
        return component;
    }
}
