package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import com.btp.budget.model.BudgetTracker;
import com.btp.budget.model.ExpenseEntry;
import com.btp.event_manager.model.Event;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// This class loads all events created by a user that were saved from
// AppDataPath.loadPath() + "/dat/[username].xml
public class LoadUserEvents {
    public static void load(AppService appService) {
        try {
            appService.getCurrUser().getEvents().clear();
            NodeList events = ReadEventsService.read(appService);
            for (int i = 0; i < events.getLength(); i++) {
                Element event = (Element) events.item(i);

                String eventName = event.getElementsByTagName("title").item(0).getTextContent();
                LocalDate startDate = LocalDate.parse(event.getElementsByTagName("startDate").item(0).getTextContent());
                LocalDate endDate = LocalDate.parse(event.getElementsByTagName("endDate").item(0).getTextContent());
                LocalDateTime lastAccessed = LocalDateTime.parse(event.getElementsByTagName("lastAccessed").item(0).getTextContent());

                Event savedEvent = new Event(eventName, startDate, endDate);
                savedEvent.setLastAccessed(lastAccessed);

                // optional
                String description;
                try {
                    description = event.getElementsByTagName("description").item(0).getTextContent();
                    savedEvent.setDescription(description);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String guests;
                try {
                    guests = event.getElementsByTagName("guests").item(0).getTextContent(); // delimited by ;
                    savedEvent.setGuests(List.of(guests.split(";")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String startTime;
                try {
                    startTime = event.getElementsByTagName("startTime").item(0).getTextContent();
                    savedEvent.setStartTime(LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String endTime;
                try {
                    endTime = event.getElementsByTagName("endTime").item(0).getTextContent();
                    savedEvent.setEndTime(LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Load budget tracker data
                Element budgetTracker = (Element) event.getElementsByTagName("budgetTracker").item(0);
                if (budgetTracker != null) {
                    List<ExpenseEntry> expenseEntries = new ArrayList<>();
                    NodeList expenseEntryNodes = budgetTracker.getElementsByTagName("expenseEntry");

                    for (int j = 0; j < expenseEntryNodes.getLength(); j++) {
                        Element expenseEntryElement = (Element) expenseEntryNodes.item(j);

                        int quantity = Integer.parseInt(expenseEntryElement.getElementsByTagName("quantity").item(0).getTextContent());
                        String itemName = expenseEntryElement.getElementsByTagName("itemName").item(0).getTextContent();
                        double costPerItem = Double.parseDouble(expenseEntryElement.getElementsByTagName("costPerItem").item(0).getTextContent());

                        ExpenseEntry expenseEntry = new ExpenseEntry(quantity, itemName, costPerItem);
                        expenseEntries.add(expenseEntry);
                    }

                    BudgetTracker tempBudgetTracker = new BudgetTracker();
                    tempBudgetTracker.setExpenses(expenseEntries);
                    savedEvent.setBudgetTracker(tempBudgetTracker);
                }
                appService.getCurrUser().getEvents().add(savedEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
