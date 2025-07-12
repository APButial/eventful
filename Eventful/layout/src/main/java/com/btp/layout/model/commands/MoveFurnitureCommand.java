package com.btp.layout.model.commands;

import com.almasb.fxgl.dsl.FXGL;
import com.btp.layout.model.Furniture;

public class MoveFurnitureCommand implements Command {
    private final Furniture furniture;
    private final int oldGridX;
    private final int oldGridY;
    private final int newGridX;
    private final int newGridY;

    public MoveFurnitureCommand(Furniture furniture, int oldGridX, int oldGridY, int newGridX, int newGridY) {
        this.furniture = furniture;
        this.oldGridX = oldGridX;
        this.oldGridY = oldGridY;
        this.newGridX = newGridX;
        this.newGridY = newGridY;
    }

    @Override
    public void execute() {
        // Update the furniture's position and grid coordinates
        furniture.setPosition(newGridX * 80, newGridY * 80); // Using CELL_SIZE constant
        furniture.setGridPosition(newGridX, newGridY);
    }

    @Override
    public boolean undo() {
        // Check if the old position is occupied
        if (isCellOccupied(oldGridX, oldGridY)) {
            return false;
        }

        // Move furniture back to its original position
        furniture.setPosition(oldGridX * 80, oldGridY * 80);
        furniture.setGridPosition(oldGridX, oldGridY);
        
        return true;
    }

    private boolean isCellOccupied(int gridX, int gridY) {
        return FXGL.getGameWorld().getEntities().stream()
                .filter(e -> e instanceof Furniture)
                .anyMatch(e -> {
                    Furniture f = (Furniture) e;
                    return f.getGridX() == gridX && f.getGridY() == gridY && f != furniture;
                });
    }
}
