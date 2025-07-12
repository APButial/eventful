package com.btp.layout.model.dto;

import com.btp.layout.model.FurnitureType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FurnitureData {
    private final FurnitureType type;
    private final int gridX;
    private final int gridY;
    private final int rotation;
    private final String imagePath;
    private final String assignedGuest;
    private final List<String> assignedGuests;
    private final String label;

    @JsonCreator
    public FurnitureData(
        @JsonProperty("type") FurnitureType type,
        @JsonProperty("gridX") int gridX,
        @JsonProperty("gridY") int gridY,
        @JsonProperty("rotation") int rotation,
        @JsonProperty("imagePath") String imagePath,
        @JsonProperty("assignedGuest") String assignedGuest,
        @JsonProperty("assignedGuests") List<String> assignedGuests,
        @JsonProperty("label") String label
    ) {
        this.type = type;
        this.gridX = gridX;
        this.gridY = gridY;
        this.rotation = rotation;
        this.imagePath = imagePath;
        this.assignedGuest = assignedGuest;
        this.assignedGuests = assignedGuests;
        this.label = label;
    }

    public FurnitureType getType() {
        return type;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getRotation() {
        return rotation;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getAssignedGuest() {
        return assignedGuest;
    }

    public List<String> getAssignedGuests() {
        return assignedGuests;
    }

    public String getLabel() {
        return label;
    }
} 