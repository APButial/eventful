package com.btp.event_manager.service;

import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.service.AppDataPath;
import com.btp.appfx.service.AppService;
import com.btp.appfx.service.CipherService;
import com.btp.budget.model.ExpenseEntry;
import com.btp.event_manager.model.Event;
import javafx.scene.control.*;
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
import java.util.Optional;

public class WriteEventsService {

    public static void write(BaseEvent newEvent, AppService appService) {
        try {
            String path = AppDataPath.loadPath() + "/dat/" + appService.getCurrUser().getUsername();
            File file = new File(path + "/events.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            Element event = document.createElement("event");

            // mandatory
            Element title = document.createElement("title");
            title.appendChild(document.createTextNode(CipherService.encrypt(newEvent.getEventName())));
            event.appendChild(title);

            Element startDate = document.createElement("startDate");
            startDate.appendChild(document.createTextNode(CipherService.encrypt(newEvent.getStartDate().toString())));
            event.appendChild(startDate);

            Element endDate = document.createElement("endDate");
            endDate.appendChild(document.createTextNode(CipherService.encrypt(newEvent.getEndDate().toString())));
            event.appendChild(endDate);

            Element status = document.createElement("status");
            status.appendChild(document.createTextNode(CipherService.encrypt(appService.getEventStatusString(newEvent.getStatus()))));
            event.appendChild(status);

            //////////////////////////////////////////////////////////////////////////////////
            // metadata
            Element creator = document.createElement("creator");
            creator.appendChild(document.createTextNode(CipherService.encrypt(appService.getCurrUser().getUsername())));
            event.appendChild(creator);

            Element userkey = document.createElement("key");
            userkey.appendChild(document.createTextNode(CipherService.encrypt(appService.getCurrUser().getUserKey())));
            event.appendChild(userkey);

            Element lastAccessed = document.createElement("lastAccessed");
            lastAccessed.appendChild(document.createTextNode(CipherService.encrypt(appService.getSysDateTime().toString())));
            event.appendChild(lastAccessed);
            //////////////////////////////////////////////////////////////////////////////////


            document.getDocumentElement().appendChild(event);


            File tempFile = new File(path + "events_temp.xml");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(tempFile);
            transformer.transform(domSource, streamResult);

            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);

            if (tempFile.exists()) {
                tempFile.delete();
            }

            System.out.println("New event added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void overwrite(AppService appService, LocalDate tempStartDate, LocalDate tempEndDate) {
        try {
            BaseEvent selectedEvent = appService.getSelectedEvent();
            String path = AppDataPath.loadPath() + "/dat/" + appService.getCurrUser().getUsername();
            File file = new File(path + "/events.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList events = CipherService.decryptXMLValues(document).getElementsByTagName("event");
            boolean eventFound = false;

            for (int i = 0; i < events.getLength(); i++) {
                Element event = (Element) events.item(i);

                // Check if the event matches the criteria
                if (selectedEvent.getEventName().equals(event.getElementsByTagName("title").item(0).getTextContent()) &&
                        selectedEvent.getStartDate().equals(LocalDate.parse(event.getElementsByTagName("startDate").item(0).getTextContent())) &&
                        selectedEvent.getEndDate().equals(LocalDate.parse(event.getElementsByTagName("endDate").item(0).getTextContent()))) {

                    // Update the event details
                    event.getElementsByTagName("title").item(0).setTextContent(selectedEvent.getEventName());

                    event.getElementsByTagName("startDate").item(0).setTextContent(tempStartDate.toString());
                    appService.setStartDate(tempStartDate);
                    event.getElementsByTagName("endDate").item(0).setTextContent(tempEndDate.toString());
                    appService.setEndDate(tempEndDate);

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
                        appService.setDescription(selectedEvent.getDescription());
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
                        appService.setStartTime(selectedEvent.getStartTime());
                    }
                    if (selectedEvent.getEndTime() != null) {
                        Element timeEnd = (Element) event.getElementsByTagName("endTime").item(0);
                        if (timeEnd != null) {
                            timeEnd.setTextContent(selectedEvent.getEndTime().toString());
                        } else {
                            timeEnd = document.createElement("endTime");
                            timeEnd.appendChild(document.createTextNode(selectedEvent.getEndTime().toString()));
                            event.appendChild(timeEnd);
                        }
                        appService.setEndTime(selectedEvent.getEndTime());
                    }
                    if (selectedEvent.getStatus() != null) {
                        Element status = (Element) event.getElementsByTagName("status").item(0);
                        if (status != null) {
                            status.setTextContent(appService.getEventStatusString(selectedEvent.getStatus()));
                        } else {
                            status = document.createElement("status");
                            status.appendChild(document.createTextNode(appService.getEventStatusString(selectedEvent.getStatus())));
                            event.appendChild(status);
                        }
                    }
                    if (selectedEvent.getGuests() != null) {
                        Element guests = (Element) event.getElementsByTagName("guests").item(0);
                        if (guests != null) {
                            guests.setTextContent(String.join(";", selectedEvent.getGuests()));
                        } else {
                            guests = document.createElement("guests");
                            guests.appendChild(document.createTextNode(String.join(";", selectedEvent.getGuests())));
                            event.appendChild(guests);
                        }
                        appService.setGuests(selectedEvent.getGuests());
                    }

                    if (((Event) selectedEvent).getBudgetTracker() != null) {
                        Element budgetTracker = (Element) event.getElementsByTagName("budgetTracker").item(0);
                        if (budgetTracker != null) {
                            // Clear existing expense entries
                            NodeList expenseEntries = budgetTracker.getElementsByTagName("expenseEntry");
                            while (expenseEntries.getLength() > 0) {
                                budgetTracker.removeChild(expenseEntries.item(0));
                            }

                            // Add new expense entries
                            for (ExpenseEntry entry : ((Event) selectedEvent).getBudgetTracker().getExpenses()) {
                                Element expenseEntry = document.createElement("expenseEntry");

                                Element quantity = document.createElement("quantity");
                                quantity.appendChild(document.createTextNode(String.valueOf(entry.getQuantity())));
                                expenseEntry.appendChild(quantity);

                                Element itemName = document.createElement("itemName");
                                itemName.appendChild(document.createTextNode(entry.getItemName()));
                                expenseEntry.appendChild(itemName);

                                Element costPerItem = document.createElement("costPerItem");
                                costPerItem.appendChild(document.createTextNode(String.valueOf(entry.getCostPerItem())));
                                expenseEntry.appendChild(costPerItem);

                                budgetTracker.appendChild(expenseEntry);
                            }
                        } else {
                            // Create a new budgetTracker element if it doesn't exist
                            budgetTracker = document.createElement("budgetTracker");
                            for (ExpenseEntry entry : ((Event) selectedEvent).getBudgetTracker().getExpenses()) {
                                Element expenseEntry = document.createElement("expenseEntry");

                                Element quantity = document.createElement("quantity");
                                quantity.appendChild(document.createTextNode(String.valueOf(entry.getQuantity())));
                                expenseEntry.appendChild(quantity);

                                Element itemName = document.createElement("itemName");
                                itemName.appendChild(document.createTextNode(entry.getItemName()));
                                expenseEntry.appendChild(itemName);

                                Element costPerItem = document.createElement("costPerItem");
                                costPerItem.appendChild(document.createTextNode(String.valueOf(entry.getCostPerItem())));
                                expenseEntry.appendChild(costPerItem);

                                budgetTracker.appendChild(expenseEntry);
                            }
                            event.appendChild(budgetTracker);
                        }
                        ((EventManAppService) appService).setBudgetTracker(((Event) selectedEvent).getBudgetTracker());
                    }

                    // Update metadata if needed
                    Element lastAccessed = (Element) event.getElementsByTagName("lastAccessed").item(0);
                    lastAccessed.setTextContent(appService.getSysDateTime().toString());

                    eventFound = true;
                    break;
                }
            }

            // If the event was found and updated, write the changes back to the file
            if (eventFound) {
                File tempFile = new File(path + "events_temp.xml");
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource domSource = new DOMSource(CipherService.encryptXMLValues(document));
                StreamResult streamResult = new StreamResult(tempFile);
                transformer.transform(domSource, streamResult);

                if (file.exists()) {
                    file.delete();
                }
                tempFile.renameTo(file);

                if (tempFile.exists()) {
                    tempFile.delete();
                }

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

    public static boolean delete(AppService appService) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Event");
        alert.setHeaderText("Are you sure you want to delete this event?");
        alert.setContentText("Confirming this action permanently deletes the event from your device.");

        // Show the dialog and wait for a response
        Optional<ButtonType> action = alert.showAndWait();
        if (action.isPresent() && action.get() != ButtonType.OK) {
            return false;
        }

        try {
            BaseEvent selectedEvent = appService.getSelectedEvent();
            String path = AppDataPath.loadPath() + "/dat/" + appService.getCurrUser().getUsername();
            File file = new File(path + "/events.xml");
            if (!file.exists()) {
                System.err.println("Events XML file does not exist.");
                return false;
            }

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            NodeList events = CipherService.decryptXMLValues(document).getElementsByTagName("event");
            boolean eventRemoved = false;

            for (int i = 0; i < events.getLength(); i++) {
                Element event = (Element) events.item(i);
                if (selectedEvent.getEventName().equals(event.getElementsByTagName("title").item(0).getTextContent()) &&
                        selectedEvent.getStartDate().equals(LocalDate.parse(event.getElementsByTagName("startDate").item(0).getTextContent())) &&
                        selectedEvent.getEndDate().equals(LocalDate.parse(event.getElementsByTagName("endDate").item(0).getTextContent()))) {
                    event.getParentNode().removeChild(event);
                    eventRemoved = true;
                    break;
                }
            }

            if (eventRemoved) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(CipherService.encryptXMLValues(document));
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);
                System.out.println("Event removed successfully.");
            } else {
                System.out.println("Event not found.");
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
