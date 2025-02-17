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

public class ValidateNewEventService {
    public static boolean validate(String eventName, LocalDate startDate, LocalDate endDate) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Create Event");

        if (eventName.isBlank()) {
            alert.setHeaderText("Invalid Event");
            alert.setContentText("Please enter event name.");
            alert.showAndWait();
            return false;
        }
        if (startDate.isAfter(endDate)) {
            alert.setHeaderText("Invalid Event");
            alert.setContentText("End date should not be earlier than Start date.");
            alert.showAndWait();
            return false;
        }

        alert.setHeaderText("Event Created");
        alert.setContentText("The event, " + eventName + ", is successfully created.");
        alert.showAndWait();
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
