package com.btp.layout.model;

public class Chair extends Furniture {
    private static final String DEFAULT_IMAGE_PATH = "/com/btp/layout/images/chair.png";

    public Chair(int gridX, int gridY) {
        this(gridX, gridY, DEFAULT_IMAGE_PATH);
    }

    public Chair(int gridX, int gridY, String imagePath) {
        super(gridX, gridY, imagePath, false, FurnitureType.CHAIR);
    }

    // Copy constructor
    private Chair(Chair other) {
        super(other);
    }

    @Override
    public Furniture createCopy() {
        return new Chair(this);
    }
} 