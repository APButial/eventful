package com.btp.layout.model;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Furniture extends Entity {
    protected static final int CELL_SIZE = 120;
    protected final int gridX;
    protected final int gridY;
    protected ImageView imageView;
    private String assignedGuest;
    private List<String> assignedGuests;
    private final boolean supportsMultipleGuests;

    public Furniture(int gridX, int gridY, String imagePath, boolean supportsMultipleGuests) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.supportsMultipleGuests = supportsMultipleGuests;
        this.assignedGuest = null;
        this.assignedGuests = supportsMultipleGuests ? new ArrayList<>() : null;
        
        // Load and set up the image
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        imageView = new ImageView(image);
        imageView.setFitWidth(CELL_SIZE - 1);
        imageView.setFitHeight(CELL_SIZE - 1);
        imageView.setPreserveRatio(true);
        
        // Set position
        setPosition(gridX * CELL_SIZE, gridY * CELL_SIZE);
        getViewComponent().addChild(imageView);

        // Add collision box
        getBoundingBoxComponent().addHitBox(new HitBox("MAIN", BoundingShape.box(CELL_SIZE - 1, CELL_SIZE - 1)));
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
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