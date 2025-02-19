package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;

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
                System.out.println("Email sent successfully.");
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
        public static void authenticate(AppService appService) {
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("Email Authentication");
            textInputDialog.setContentText("Email Address");
            textInputDialog.setContentText("Please enter your email address:");

            Optional<String> result = textInputDialog.showAndWait();
            result.ifPresent(appService::setEmailAdd);

            textInputDialog = new TextInputDialog();
            textInputDialog.getDialogPane().setContent(new PasswordField());
            textInputDialog.setTitle("Email Authentication");
            textInputDialog.setHeaderText("Email App Password");
            textInputDialog.setContentText("Please enter your email app password:");
            result = textInputDialog.showAndWait();
            result.ifPresent(appService::setEmailPass);
        }
}
