package com.btp.layout.model;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public abstract class Furniture extends Entity {
    protected static final int CELL_SIZE = 40;
    protected final int gridX;
    protected final int gridY;
    protected Rectangle shape;
    private String assignedGuest;
    private List<String> assignedGuests;
    private final boolean supportsMultipleGuests;

    public Furniture(int gridX, int gridY, Color color, boolean supportsMultipleGuests) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.supportsMultipleGuests = supportsMultipleGuests;
        this.assignedGuest = null;
        this.assignedGuests = supportsMultipleGuests ? new ArrayList<>() : null;
        
        shape = new Rectangle(CELL_SIZE - 1, CELL_SIZE - 1);
        shape.setFill(color);
        
        // Set position
        setPosition(gridX * CELL_SIZE, gridY * CELL_SIZE);
        getViewComponent().addChild(shape);

        // Add collision box
        getBoundingBoxComponent().addHitBox(new HitBox("MAIN", BoundingShape.box(CELL_SIZE - 1, CELL_SIZE - 1)));
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setColor(Color color) {
        shape.setFill(color);
    }

    public Color getColor() {
        return (Color) shape.getFill();
    }

    public boolean supportsMultipleGuests() {
        return supportsMultipleGuests;
    }

    public String getAssignedGuest() {
        return assignedGuest;
    }

    public void setAssignedGuest(String guest) {
        if (!supportsMultipleGuests) {
            this.assignedGuest = guest;
        }
    }

    public List<String> getAssignedGuests() {
        return supportsMultipleGuests ? new ArrayList<>(assignedGuests) : null;
    }

    public void addAssignedGuest(String guest) {
        if (supportsMultipleGuests && guest != null) {
            assignedGuests.add(guest);
        }
    }

    public void removeAssignedGuest(String guest) {
        if (supportsMultipleGuests && guest != null) {
            assignedGuests.remove(guest);
        }
    }

    public void clearAssignedGuests() {
        if (supportsMultipleGuests) {
            assignedGuests.clear();
        }
    }
} 