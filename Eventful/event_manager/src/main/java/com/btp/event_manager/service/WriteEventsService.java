package com.btp.event_manager.service;

import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.service.AppService;
import com.btp.event_manager.model.Event;
import javafx.scene.control.Alert;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

public class WriteEventsService {
    public static void write(BaseEvent newEvent, AppService appService) {
        try {
            File file = new File("Eventful/dat/" + appService.getCurrUser().getUsername() + "/events.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            Element event = document.createElement("event");

            // mandatory
            Element title = document.createElement("title");
            title.appendChild(document.createTextNode(newEvent.getEventName()));
            event.appendChild(title);

            Element startDate = document.createElement("startDate");
            startDate.appendChild(document.createTextNode(newEvent.getStartDate().toString()));
            event.appendChild(startDate);

            Element endDate = document.createElement("endDate");
            endDate.appendChild(document.createTextNode(newEvent.getEndDate().toString()));
            event.appendChild(endDate);

            //////////////////////////////////////////////////////////////////////////////////
            // metadata
            Element creator = document.createElement("creator");
            creator.appendChild(document.createTextNode(appService.getCurrUser().getUsername()));
            event.appendChild(creator);

            Element lastAccessed = document.createElement("lastAccessed");
            lastAccessed.appendChild(document.createTextNode(appService.getSysDateTime().toString()));
            event.appendChild(lastAccessed);
            //////////////////////////////////////////////////////////////////////////////////


            document.getDocumentElement().appendChild(event);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource,streamResult);
            System.out.println("New event added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void overwrite(AppService appService) {
        try {
            BaseEvent selectedEvent = appService.getSelectedEvent();
            File file = new File("Eventful/dat/" + appService.getCurrUser().getUsername() + "/events.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList events = document.getElementsByTagName("event");
            boolean eventFound = false;

            for (int i = 0; i < events.getLength(); i++) {
                Element event = (Element) events.item(i);

                // Check if the event matches the criteria
                if (selectedEvent.getEventName().equals(event.getElementsByTagName("title").item(0).getTextContent()) &&
                        selectedEvent.getStartDate().equals(LocalDate.parse(event.getElementsByTagName("startDate").item(0).getTextContent())) &&
                        selectedEvent.getEndDate().equals(LocalDate.parse(event.getElementsByTagName("endDate").item(0).getTextContent()))) {

                    // Update the event details
                    event.getElementsByTagName("title").item(0).setTextContent(selectedEvent.getEventName());
                    event.getElementsByTagName("startDate").item(0).setTextContent(selectedEvent.getStartDate().toString());
                    event.getElementsByTagName("endDate").item(0).setTextContent(selectedEvent.getEndDate().toString());

                    // Optional
                    if (selectedEvent.getDescription() != null) {
                        Element description = (Element) event.getElementsByTagName("description").item(0);
                        if (description != null) {
                            description.setTextContent(selectedEvent.getDescription());
                        } else {
                            description = document.createElement("description");
                            description.appendChild(document.createTextNode(selectedEvent.getDescription()));
                            event.appendChild(description);
                        }
                    }
                    if (selectedEvent.getStartTime() != null) {
                        Element timeStart = (Element) event.getElementsByTagName("startTime").item(0);
                        if (timeStart != null) {
                            timeStart.setTextContent(selectedEvent.getStartTime().toString());
                        } else {
                            timeStart = document.createElement("startTime");
                            timeStart.appendChild(document.createTextNode(selectedEvent.getStartTime().toString()));
                            event.appendChild(timeStart);
                        }
                    }
                    if (appService.getEndTime() != null) {
                        Element timeEnd = (Element) event.getElementsByTagName("endTime").item(0);
                        if (timeEnd != null) {
                            timeEnd.setTextContent(selectedEvent.getEndTime().toString());
                        } else {
                            timeEnd = document.createElement("endTime");
                            timeEnd.appendChild(document.createTextNode(selectedEvent.getEndTime().toString()));
                            event.appendChild(timeEnd);
                        }
                    }
                    if (selectedEvent.getGuests() != null) {
                        Element guests = (Element)  event.getElementsByTagName("guests").item(0);
                        if (guests != null) {
                            guests.setTextContent(String.join(";", selectedEvent.getGuests()));
                        } else {
                            guests = document.createElement("guests");
                            guests.appendChild(document.createTextNode(String.join(";", selectedEvent.getGuests())));
                            event.appendChild(guests);
                        }
                        System.out.println("guest updated");
                    }

                    // Update metadata if needed
                    event.getElementsByTagName("lastAccessed").item(0).setTextContent(appService.getSysDateTime().toString());

                    eventFound = true;
                    break; // Exit the loop once the event is found and updated
                }
            }

            // If the event was found and updated, write the changes back to the file
            if (eventFound) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource domSource = new DOMSource(document);
                StreamResult streamResult = new StreamResult(file);
                transformer.transform(domSource, streamResult);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event Detail");
                alert.setHeaderText("Updated Successful");
                alert.setContentText("The event details of this event has been successfully updated.");
                alert.showAndWait();
                System.out.println("Event updated successfully");
            } else {
                System.out.println("Event not found for update");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
