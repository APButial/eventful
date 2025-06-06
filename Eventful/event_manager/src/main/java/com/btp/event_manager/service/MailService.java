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

                // Create the email body with HTML
                String body = "<html><body>" +
                        "<p>You have been invited to attend " + email + "'s " + appService.getEventName().toUpperCase() + " on " +
                        appService.getStartDate().getMonth() + " " + appService.getStartDate().getDayOfMonth() + ", " + appService.getStartDate().getYear() + " to " +
                        appService.getEndDate().getMonth() + " " + appService.getEndDate().getDayOfMonth() + ", " + appService.getEndDate().getYear() + ". See you there!</p>" +

                        // RSVP buttons
                        "<a href='http://www.eventful.com/rsvp/accept?eventId=123&email=" + email + "' style='display:inline-block; padding:10px 20px; font-size:12px; color:white; background-color:#8425a4; text-decoration:none; border-radius:5px; margin-right: 10px;'>Accept</a>" +
                        "<a href='http://www.eventful.com/rsvp/decline?eventId=123&email=" + email + "' style='display:inline-block; padding:10px 20px; font-size:12px; color:white; background-color:#8425a4; text-decoration:none; border-radius:5px;'>Decline</a>" +

                        "<br><br>" +
                        "<hr>" +
                        "<p>This is an auto-generated mail sent from Eventful. Please contact the sender for more details.</p>" +
                        "</body></html>";

                // Set the content type to HTML
                message.setContent(body, "text/html");

                Transport.send(message);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event Detail");
                alert.setHeaderText("Invitation Sent Successfully");
                alert.setContentText("Your event invitation is sent successfully to entered email addresses.");
                alert.showAndWait();
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

}
