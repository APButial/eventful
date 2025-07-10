package com.btp.layout.model.commands;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.btp.layout.model.Furniture;
import com.btp.layout.model.GuestList;
import com.btp.layout.model.Table;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EraseCommand implements Command {
    private final Furniture originalFurniture;
    private final Furniture furnitureCopy;
    private final String assignedGuest;
    private final List<String> assignedGuests;
    private final int savedRotation; // Store the rotation state

    public EraseCommand(Furniture furniture) {
        this.originalFurniture = furniture;
        this.furnitureCopy = furniture.createCopy(); // Create deep copy
        this.assignedGuest = furniture.getAssignedGuest();
        this.assignedGuests = furniture.getAssignedGuests() != null ? 
                             new ArrayList<>(furniture.getAssignedGuests()) : 
                             null;
        this.savedRotation = furniture.getFurnitureRotation(); // Save the rotation state
    }

    @Override
    public void execute() {
        // Unassign guests before removing furniture
        GuestList guestList = GuestList.getInstance();
        if (assignedGuest != null) {
            guestList.unassignFromChair(assignedGuest);
        }
        if (assignedGuests != null) {
            for (String guest : assignedGuests) {
                guestList.unassignFromTable(guest);
            }
        }
        FXGL.getGameWorld().removeEntity(originalFurniture);
    }

    private boolean isCellOccupied(int gridX, int gridY) {
        return FXGL.getGameWorld().getEntities().stream()
                .filter(e -> e instanceof Furniture)
                .anyMatch(e -> {
                    Furniture f = (Furniture) e;
                    return f.getGridX() == gridX && f.getGridY() == gridY;
                });
    }

    @Override
    public boolean undo() {
        // First check if the cell is occupied
        if (isCellOccupied(furnitureCopy.getGridX(), furnitureCopy.getGridY())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cell Occupied");
            alert.setHeaderText(null);
            alert.setContentText("Cannot undo because the cell (" + 
                               furnitureCopy.getGridX() + ", " + 
                               furnitureCopy.getGridY() + ") is already occupied.");
            alert.showAndWait();
            return false;
        }

        // Check if any guests are already assigned
        GuestList guestList = GuestList.getInstance();
        boolean hasConflict = false;
        List<String> conflictingGuests = new ArrayList<>();

        // Only check conflicts for guests that still exist in the guest list
        if (assignedGuest != null && guestList.getAllGuests().contains(assignedGuest) && 
            guestList.isGuestAssignedToChair(assignedGuest)) {
            hasConflict = true;
            conflictingGuests.add(assignedGuest);
        }
        
        if (assignedGuests != null) {
            for (String guest : assignedGuests) {
                if (guestList.getAllGuests().contains(guest) && 
                    guestList.isGuestAssignedToTable(guest)) {
                    hasConflict = true;
                    conflictingGuests.add(guest);
                }
            }
        }

        if (hasConflict) {
            // Show conflict alert
            StringBuilder message = new StringBuilder("Cannot undo because the following guests are already assigned:\n");
            for (String guest : conflictingGuests) {
                message.append("- ").append(guest).append("\n");
            }
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Guest Assignment Conflict");
            alert.setHeaderText(null);
            alert.setContentText(message.toString());
            alert.showAndWait();
            return false; // Undo failed
        }

        // No conflicts, proceed with undo using the copy
        FXGL.getGameWorld().addEntity(furnitureCopy);
        
        // Restore the rotation state
        furnitureCopy.setRotation(savedRotation);

        // Reassign only existing guests, removing duplicates
        if (assignedGuest != null && guestList.getAllGuests().contains(assignedGuest)) {
            furnitureCopy.setAssignedGuest(assignedGuest);
            guestList.assignGuestToChair(assignedGuest, furnitureCopy);
        }
        
        if (assignedGuests != null && furnitureCopy instanceof Table) {
            Table table = (Table) furnitureCopy;
            
            // Filter out guests that no longer exist and remove duplicates while preserving order
            Set<String> uniqueExistingGuests = assignedGuests.stream()
                .filter(guest -> guestList.getAllGuests().contains(guest))
                .collect(Collectors.toCollection(LinkedHashSet::new));
            
            // Clear existing guests and add unique ones that still exist
            furnitureCopy.clearAssignedGuests();
            for (String guest : uniqueExistingGuests) {
                furnitureCopy.addAssignedGuest(guest);
                guestList.assignGuestToTable(guest, table);
            }
        }
        
        return true; // Undo succeeded
    }
} 