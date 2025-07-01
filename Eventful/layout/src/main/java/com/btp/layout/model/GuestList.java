package com.btp.layout.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GuestList {
    private static GuestList instance;
    private final List<String> guests;
    private final Set<String> assignedToChairs;
    private final Set<String> assignedToTables;
    private final Map<String, Furniture> guestToChair;
    private final Map<String, Table> guestToTable;

    private GuestList() {
        guests = new ArrayList<>();
        assignedToChairs = new HashSet<>();
        assignedToTables = new HashSet<>();
        guestToChair = new HashMap<>();
        guestToTable = new HashMap<>();
    }

    public static GuestList getInstance() {
        if (instance == null) {
            instance = new GuestList();
        }
        return instance;
    }

    public void addGuest(String name) {
        guests.add(name);
    }

    public void removeGuest(String name) {
        guests.remove(name);
        
        // Remove from chair if assigned
        Furniture chair = guestToChair.remove(name);
        if (chair != null) {
            chair.setAssignedGuest(null);
            assignedToChairs.remove(name);
        }
        
        // Remove from table if assigned
        Table table = guestToTable.remove(name);
        if (table != null) {
            table.removeAssignedGuest(name);
            assignedToTables.remove(name);
        }
    }

    public List<String> getGuests() {
        return new ArrayList<>(guests);
    }

    public List<String> getUnassignedGuestsForChair() {
        List<String> unassigned = new ArrayList<>(guests);
        unassigned.removeAll(assignedToChairs);
        return unassigned;
    }

    public List<String> getUnassignedGuestsForTable() {
        List<String> unassigned = new ArrayList<>(guests);
        unassigned.removeAll(assignedToTables);
        return unassigned;
    }

    public void assignGuestToChair(String guest, Furniture chair) {
        if (guests.contains(guest) && chair != null) {
            // Remove from previous chair if any
            Furniture previousChair = guestToChair.get(guest);
            if (previousChair != null) {
                previousChair.setAssignedGuest(null);
            }
            
            assignedToChairs.add(guest);
            guestToChair.put(guest, chair);
        }
    }

    public void assignGuestToTable(String guest, Table table) {
        if (guests.contains(guest) && table != null) {
            // Remove from previous table if any
            Table previousTable = guestToTable.get(guest);
            if (previousTable != null) {
                previousTable.removeAssignedGuest(guest);
            }
            
            assignedToTables.add(guest);
            guestToTable.put(guest, table);
        }
    }

    public void unassignFromChair(String guest) {
        assignedToChairs.remove(guest);
        guestToChair.remove(guest);
    }

    public void unassignFromTable(String guest) {
        assignedToTables.remove(guest);
        guestToTable.remove(guest);
    }

    public boolean isGuestAssignedToChair(String guest) {
        return assignedToChairs.contains(guest);
    }

    public boolean isGuestAssignedToTable(String guest) {
        return assignedToTables.contains(guest);
    }
} 