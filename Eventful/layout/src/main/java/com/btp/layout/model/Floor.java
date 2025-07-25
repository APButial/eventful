package com.btp.layout.model;

public class Floor extends Furniture {
    private static final String DEFAULT_IMAGE_PATH = "/com/btp/layout/images/floor.png";

    public Floor(int gridX, int gridY) {
        this(gridX, gridY, DEFAULT_IMAGE_PATH);
    }

    public Floor(int gridX, int gridY, String imagePath) {
        super(gridX, gridY, imagePath, false, FurnitureType.FLOOR, "F");
    }
    // Copy constructor
    private Floor(Floor other) {
        super(other);
    }

    @Override
    public Furniture createCopy() {
        return new Floor(this);
    }
} 