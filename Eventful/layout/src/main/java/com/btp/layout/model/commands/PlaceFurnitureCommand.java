package com.btp.layout.model.commands;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.btp.layout.model.Furniture;
import com.btp.layout.model.GuestList;
import java.util.Optional;

public class PlaceFurnitureCommand implements Command {
    private final Furniture furniture;
    private final int gridX;
    private final int gridY;

    public PlaceFurnitureCommand(Furniture furniture) {
        this.furniture = furniture;
        this.gridX = furniture.getGridX();
        this.gridY = furniture.getGridY();
    }

    @Override
    public void execute() {
        FXGL.getGameWorld().addEntity(furniture);
        // Relabel all after placement
        if (furniture.getFurnitureType() == com.btp.layout.model.FurnitureType.CHAIR ||
            furniture.getFurnitureType() == com.btp.layout.model.FurnitureType.TABLE) {
            com.btp.layout.model.FurnitureFactory.relabelFurniture(
                FXGL.getGameWorld().getEntities().stream().toList()
            );
        }
    }

    private Optional<Entity> findFurnitureAt(int gridX, int gridY) {
        return FXGL.getGameWorld().getEntities().stream()
                .filter(e -> e instanceof Furniture)
                .filter(e -> {
                    Furniture f = (Furniture) e;
                    return f.getGridX() == gridX && f.getGridY() == gridY;
                })
                .findFirst();
    }

    @Override
    public boolean undo() {
        // Find and remove any furniture at the target location
        Optional<Entity> existingFurniture = findFurnitureAt(gridX, gridY);
        
        if (existingFurniture.isPresent()) {
            Furniture furnitureToRemove = (Furniture) existingFurniture.get();
            
            // Unassign any guests before removing
            GuestList guestList = GuestList.getInstance();
            String singleGuest = furnitureToRemove.getAssignedGuest();
            if (singleGuest != null) {
                guestList.unassignFromChair(singleGuest);
            }
            
            var multipleGuests = furnitureToRemove.getAssignedGuests();
            if (multipleGuests != null) {
                for (String guest : multipleGuests) {
                    guestList.unassignFromTable(guest);
                }
            }
            
            // Remove the furniture
            FXGL.getGameWorld().removeEntity(furnitureToRemove);
            // Decrement and relabel
            if (furnitureToRemove.getFurnitureType() == com.btp.layout.model.FurnitureType.CHAIR) {
                com.btp.layout.model.FurnitureFactory.decrementChairCount();
            } else if (furnitureToRemove.getFurnitureType() == com.btp.layout.model.FurnitureType.TABLE) {
                com.btp.layout.model.FurnitureFactory.decrementTableCount();
            }
            com.btp.layout.model.FurnitureFactory.relabelFurniture(
                FXGL.getGameWorld().getEntities().stream().toList()
            );
        }
        
        return true; // Undo always succeeds
    }
} 