package com.btp.layout.model.commands;

import com.almasb.fxgl.dsl.FXGL;
import com.btp.layout.model.Furniture;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveGroupCommand implements Command {
    private static final int CELL_SIZE = 80;
    
    private final List<Furniture> furnitureGroup;
    private final Map<Furniture, Point2D> oldPositions;
    private final Map<Furniture, Point2D> newPositions;

    public MoveGroupCommand(List<Furniture> furnitureGroup, 
                           Map<Furniture, Point2D> oldPositions, 
                           Map<Furniture, Point2D> newPositions) {
        this.furnitureGroup = furnitureGroup;
        this.oldPositions = new HashMap<>(oldPositions);
        this.newPositions = new HashMap<>(newPositions);
    }

    @Override
    public void execute() {
        // Move all furniture to their new positions
        furnitureGroup.forEach(furniture -> {
            Point2D newPos = newPositions.get(furniture);
            if (newPos != null) {
                furniture.setPosition(newPos.getX() * CELL_SIZE, newPos.getY() * CELL_SIZE);
                furniture.setGridPosition((int) newPos.getX(), (int) newPos.getY());
            }
        });
    }

    @Override
    public boolean undo() {
        // Check if all old positions are available
        for (Furniture furniture : furnitureGroup) {
            Point2D oldPos = oldPositions.get(furniture);
            if (oldPos != null && isCellOccupiedExcluding((int) oldPos.getX(), (int) oldPos.getY(), furnitureGroup)) {
                return false; // Cannot undo if any position is occupied
            }
        }

        // Move all furniture back to their original positions
        furnitureGroup.forEach(furniture -> {
            Point2D oldPos = oldPositions.get(furniture);
            if (oldPos != null) {
                furniture.setPosition(oldPos.getX() * CELL_SIZE, oldPos.getY() * CELL_SIZE);
                furniture.setGridPosition((int) oldPos.getX(), (int) oldPos.getY());
            }
        });
        
        return true;
    }

    private boolean isCellOccupiedExcluding(int gridX, int gridY, List<Furniture> excluding) {
        return FXGL.getGameWorld().getEntities().stream()
                .filter(e -> e instanceof Furniture)
                .filter(e -> !excluding.contains(e))
                .anyMatch(e -> {
                    Furniture furniture = (Furniture) e;
                    return furniture.getGridX() == gridX && furniture.getGridY() == gridY;
                });
    }
}
