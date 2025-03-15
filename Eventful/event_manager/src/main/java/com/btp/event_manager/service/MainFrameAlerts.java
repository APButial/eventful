package com.btp.event_manager.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class MainFrameAlerts {
    public static boolean saveChanges() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Event Detail");
        alert.setHeaderText("Unsaved Changes");
        alert.setContentText("Would you like to save changes before leaving the event's details?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()) {
            if(result.get() == ButtonType.YES) {
                return true;
            }
        }
        return false;
    }
    public static boolean sendEmailConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Event Detail");
        alert.setHeaderText("Sending Invitation");
        alert.setContentText("You are about to send an invitation to all email addresses listed. Would you like to proceed?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()) {
            if(result.get() == ButtonType.YES) {
                return true;
            }
        }
        return false;
    }
}
