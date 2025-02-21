package com.btp.event_manager.service;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexService {
    private static final String emailPattern = "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$";

    public static String parse(String input) {
        List<String> emails = new ArrayList<>(List.of(input.split(";")));

        Pattern pattern = Pattern.compile(emailPattern);
        int validEmailCount = 0;

        for (String email : emails) {
            Matcher matcher = pattern.matcher(email.trim());
            if (matcher.matches()) {
                validEmailCount++;
            }
        }

        return String.valueOf(validEmailCount);
    }
    public static boolean validate(String input) {
        List<String> emails = new ArrayList<>(List.of(input.strip().split(";")));

        Pattern pattern = Pattern.compile(emailPattern);
        for (String email : emails) {
            Matcher matcher = pattern.matcher(email.trim());
            if (!matcher.matches()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event Detail");
                alert.setHeaderText("Invalid Email");
                alert.setContentText("An invalid email has been entered. Make sure that a semi-colon (;) correctly appends the email addresses.");
                alert.showAndWait();

                return false;
            }
        }

        return true;
    }
}
