package com.btp.login.service;

import com.btp.appfx.service.AppService;
import com.btp.appfx.service.CipherService;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class RemoveUserService {
    public static boolean delete(AppService appService) {
        Optional<String> input;

        Dialog<String> passDialog = new Dialog<>();
        passDialog.setTitle("Remove User Account");
        passDialog.setHeaderText("Enter User Password to Confirm Account Removal");
        passDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField pwd = new PasswordField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Please enter your password:"), pwd);
        passDialog.getDialogPane().setContent(content);
        passDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return pwd.getText();
            }
            return null;
        });

        input = passDialog.showAndWait();
        if (input.isEmpty()) {
            return false; // User canceled the dialog
        }

        try {
            File file = new File("Eventful - Event Management System/dat/users.xml");
            if (!file.exists()) {
                System.err.println("User XML file does not exist.");
                return false;
            }

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList users = CipherService.decryptXMLValues(document).getElementsByTagName("user");
            boolean userRemoved = false;

            for (int i = 0; i < users.getLength(); i++) {
                Element tempUser = (Element) users.item(i);
                String username = tempUser.getElementsByTagName("username").item(0).getTextContent();
                String password = tempUser.getElementsByTagName("password").item(0).getTextContent().trim();

                if (username.equals(appService.getCurrUser().getUsername())) {
                    if (!password.equals(input.get())) {
                        return false; // Password does not match
                    } else {
                        tempUser.getParentNode().removeChild(tempUser);
                        userRemoved = true;
                        break;
                    }
                }
            }

            if (userRemoved) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);
                System.out.println("User removed successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            return false;
        }

        // Delete user directory
        Path userDir = Paths.get("Eventful - Event Management System/dat/" + appService.getCurrUser().getUsername());
        try {
            Files.walk(userDir).sorted((p1, p2) -> -p1.compareTo(p2)).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    System.err.println("Failed to delete: " + path + " - " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error deleting directory: " + e.getMessage());
        }
        return true;
    }
}
