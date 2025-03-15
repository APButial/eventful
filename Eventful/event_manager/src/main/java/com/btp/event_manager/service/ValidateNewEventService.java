package com.btp.event_manager.service;


import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.service.AppService;
import com.btp.event_manager.model.Event;
import com.btp.login.service.InitUsersXml;
import com.btp.login.service.PassHashService;
import com.btp.login.service.ReadUsersService;
import com.btp.login.service.WriteUsersService;
import javafx.scene.control.Alert;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ValidateNewEventService {
    public static boolean validate(String eventName, LocalDate startDate, LocalDate endDate, AppService appService) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Create Event");

        if (eventName.isBlank()) {
            alert.setHeaderText("Invalid Event");
            alert.setContentText("Please enter event name.");
            alert.showAndWait();
            return false;
        }

        try {
            NodeList events = ReadEventsService.read(appService);
            for (int i = 0; i < events.getLength(); i++) {
                Element event = (Element) events.item(i);

                if (eventName.equals(event.getElementsByTagName("title").item(0).getTextContent()) &&
                    startDate.equals(LocalDate.parse(event.getElementsByTagName("startDate").item(0).getTextContent())) &&
                    endDate.equals(LocalDate.parse(event.getElementsByTagName("endDate").item(0).getTextContent()))) {
                    alert.setHeaderText("Invalid Event");
                    alert.setContentText("An event already exists with the entered details.");
                    alert.showAndWait();
                    return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
    public static void addEvent(BaseEvent event, AppService appService) {
        NodeList events = ReadEventsService.read(appService);

        if(events == null) {
            InitEventsXML.init(event, appService);
        } else{
            WriteEventsService.write(event, appService);
        }
    }
}
