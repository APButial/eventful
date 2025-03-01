package com.btp.dashboard.component;

import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class LogsArea{
    private VBox component;
    private TextArea logArea;

    public LogsArea() {
        component = new VBox();

        logArea = new TextArea();
        logArea.setWrapText(true);
        logArea.setEditable(false);

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
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        component.getChildren().add(scrollPane);
    }

    public VBox getComponent() {
        return component;
    }

    public TextArea getLogArea() {
        return logArea;
    }
}
