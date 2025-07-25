package com.btp.layout.model;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Furniture extends Entity {
    protected static final int CELL_SIZE = 80;
    protected int gridX;  // Removed final modifier
    protected int gridY;  // Removed final modifier
    protected ImageView imageView;
    private String assignedGuest;
    private List<String> assignedGuests;
    private final boolean supportsMultipleGuests;
    private String imagePath; // Added to store image path for copying
    private int rotation = 0; // 0 = 0°, 1 = 90°, 2 = 180°, 3 = 270°
    private final FurnitureType type; // Added type field
    private Label cellLabel; // Cell label displayed on top of furniture
    private String furnitureLabel; // Label like C1, T1, ...

    public Furniture(int gridX, int gridY, String imagePath, boolean supportsMultipleGuests, FurnitureType type, String furnitureLabel) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.imagePath = imagePath; // Store the image path
        this.supportsMultipleGuests = supportsMultipleGuests;
        this.assignedGuest = null;
        this.assignedGuests = supportsMultipleGuests ? new ArrayList<>() : null;
        this.type = type;
        this.furnitureLabel = furnitureLabel;
        
        // Set lower z-index so grid appears on top
        this.setZIndex(100);
        
        setupImage(imagePath);
        setupCellLabel();
    }

    // Copy constructor
    protected Furniture(Furniture other) {
        this.gridX = other.gridX;
        this.gridY = other.gridY;
        this.imagePath = other.imagePath;
        this.supportsMultipleGuests = other.supportsMultipleGuests;
        this.type = other.type;
        this.furnitureLabel = other.furnitureLabel;
        
        // Deep copy of assigned guests
        this.assignedGuest = other.assignedGuest;
        if (other.assignedGuests != null) {
            this.assignedGuests = new ArrayList<>(other.assignedGuests);
        }
        
        setupImage(imagePath);
        setupCellLabel();
    }

    private void setupImage(String imagePath) {
        // Load and set up the image
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        imageView = new ImageView(image);
        // Change from CELL_SIZE - 1 to CELL_SIZE for full cell coverage
        imageView.setFitWidth(CELL_SIZE);
        imageView.setFitHeight(CELL_SIZE);
        imageView.setPreserveRatio(true);
        
        // Set position
        setPosition(gridX * CELL_SIZE, gridY * CELL_SIZE);
        getViewComponent().addChild(imageView);

        // Update collision box to match new size
        getBoundingBoxComponent().addHitBox(new HitBox("MAIN", BoundingShape.box(CELL_SIZE, CELL_SIZE)));
    }

    private void setupCellLabel() {
        if (cellLabel != null) {
            getViewComponent().removeChild(cellLabel);
        }
        String cell = furnitureLabel != null ? furnitureLabel : "";
        cellLabel = new Label(cell);
        cellLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30)); // Decreased size for less padding
        cellLabel.setTextFill(Color.rgb(0, 0, 0, 0.9)); // 10% more transparent (alpha 0.9)
        cellLabel.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-padding: 0 1 0 1; -fx-border-radius: 4; -fx-background-radius: 4;");
        // Update positioning for new cell size
        cellLabel.setTranslateX(CELL_SIZE / 2.0 - 20); // Adjust for new label width
        cellLabel.setTranslateY(CELL_SIZE / 2.0 - 15); // Center vertically for smaller font
        cellLabel.setVisible(false); // Hide by default
        getViewComponent().addChild(cellLabel);
    }

    // Abstract method for creating copies
    public abstract Furniture createCopy();

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridPosition(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        setPosition(gridX * CELL_SIZE, gridY * CELL_SIZE);
        if (cellLabel != null) {
            cellLabel.setText(furnitureLabel);
        }
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

    public void rotate() {
        rotation = (rotation + 1) % 4;
        imageView.setRotate(rotation * 90);
    }

    public void setRotation(int newRotation) {
        rotation = newRotation % 4;
        imageView.setRotate(rotation * 90);
    }

    public int getFurnitureRotation() {
        return rotation;
    }

    public double getRotationAngle() {
        return rotation * 90;
    }

    public FurnitureType getFurnitureType() {
        return type;
    }
    
    public void showCellLabel() {
        if (cellLabel != null) {
            cellLabel.setVisible(true);
        }
    }
    
    public void hideCellLabel() {
        if (cellLabel != null) {
            cellLabel.setVisible(false);
        }
    }
    
    public static void showAllCellLabels() {
        // This will be called from the main app to show all labels
    }
    
    public static void hideAllCellLabels() {
        // This will be called from the main app to hide all labels
    }

    public String getFurnitureLabel() {
        return furnitureLabel;
    }
    public void setFurnitureLabel(String label) {
        this.furnitureLabel = label;
        if (cellLabel != null) {
            cellLabel.setText(label);
        }
    }
    public String getImagePath() {
        return imagePath;   
    }
} 