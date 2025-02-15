package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import com.btp.event_manager.model.Event;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.time.LocalDateTime;

// This class loads all events created by a user that were saved from
// Eventful/dat/[username].xml
public class LoadUserEvents {
    public static void load(AppService appService) {
        NodeList events = ReadEventsService.read(appService);
        for (int i = 0; i < events.getLength(); i++) {
            Element event = (Element) events.item(i);

            String eventName = event.getElementsByTagName("title").item(0).getTextContent();
            LocalDate startDate = LocalDate.parse(event.getElementsByTagName("startDate").item(0).getTextContent());
            LocalDate endDate = LocalDate.parse(event.getElementsByTagName("endDate").item(0).getTextContent());
            LocalDateTime lastAccessed = LocalDateTime.parse(event.getElementsByTagName("lastAccessed").item(0).getTextContent());

            Event savedEvent = new Event(eventName, startDate, endDate);
            savedEvent.setLastAccessed(lastAccessed);

            appService.getCurrUser().getEvents().add(savedEvent);
        }
    }
}
