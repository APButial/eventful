package com.btp.login.service;

import javafx.scene.control.Alert;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class VerifyLoginService {
    public static boolean verify(String username, String password) {
        try {
            NodeList users = ReadUsersService.read();
            for (int i = 0; i < users.getLength(); i++) {
                Element tempUser = (Element) users.item(i);
                if(username.equals(tempUser.getElementsByTagName("username").item(0).getTextContent())) {
                    if (password.equals(tempUser.getElementsByTagName("password").item(0).getTextContent()))
                        return true;
                }
            }
            return  false;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login");
            alert.setHeaderText("No local users found.");
            alert.setContentText("Please create a new account and try again.");
            alert.showAndWait();
            return false;
        }
    }
}
