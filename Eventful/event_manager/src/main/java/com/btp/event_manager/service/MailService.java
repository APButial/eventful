package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class MailService {
        public static void sendMail(AppService appService) throws AddressException {
            String email = appService.getEmailAdd();
            String pass = appService.getEmailPass();

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host
            props.put("mail.smtp.port", "587"); // SMTP port (TLS)
            props.put("mail.smtp.auth", "true"); // Enable authentication
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, pass);
                }
            });

            InternetAddress[] addresses = new InternetAddress[appService.getGuests().size()];
            for (int i = 0; i < appService.getGuests().size(); i++) {
                addresses[i] = new InternetAddress(appService.getGuests().get(i));
            }

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.setRecipients(Message.RecipientType.TO, addresses);
                message.setSubject("Eventful - You Have Been Invited to an Event!");
                message.setText("You have been invited to attend " + email + "'s " + appService.getEventName().toUpperCase() + " on " +
                                appService.getStartDate().getMonth() + " " + appService.getStartDate().getDayOfMonth() + ", " + appService.getStartDate().getYear() + " to " +
                                appService.getEndDate().getMonth() + " " + appService.getEndDate().getDayOfMonth() + ", " + appService.getStartDate().getYear() + ". See you there!\n" +
                                "This is an auto-generated mail sent from Eventful. Please contact the sender for more details.");

                Transport.send(message);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event Detail");
                alert.setHeaderText("Invitation Sent Successfully");
                alert.setContentText("Your event invitation is sent successfully to entered email addresses.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static boolean validMailArea(String mail, AppService appService) {
            String processedMail = mail.strip().toLowerCase();
            System.out.println(mail);
            if(!processedMail.contains(";")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event Detail");
                alert.setHeaderText("Invalid Email Address Syntax");
                alert.setContentText("Each email address must be appended by a semicolon (;).");
                alert.showAndWait();
                return false;
            }

            appService.setGuests(List.of(processedMail.split(";")));
            System.out.println("Processed emails");
            return true;
        }
        public static boolean authenticate(AppService appService) {
            TextInputDialog emailDialog = new TextInputDialog();
            emailDialog.setTitle("Email Authentication");
            emailDialog.setHeaderText("Email Address");
            emailDialog.setContentText("Please enter your email address:");

            Optional<String> result = emailDialog.showAndWait();
            if (result.isEmpty()) {return false;}
            result.ifPresent(appService::setEmailAdd);

            Dialog<String> passDialog = new Dialog<>();
            passDialog.setTitle("Email Authentication");
            passDialog.setHeaderText("Email Password");
            passDialog.setGraphic(emailDialog.getGraphic()); // Custom graphic
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

            result = passDialog.showAndWait();
            if (result.isEmpty()) {return false;}
            result.ifPresent(appService::setEmailPass);

            return true;
        }
}
