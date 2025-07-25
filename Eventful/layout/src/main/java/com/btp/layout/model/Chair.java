package com.btp.layout.model;

public class Chair extends Furniture {
    private static final String DEFAULT_IMAGE_PATH = "/com/btp/layout/images/chair.png";

    public Chair(int gridX, int gridY, String imagePath, String label) {
        super(gridX, gridY, imagePath, false, FurnitureType.CHAIR, label);
    }

    public Chair(int gridX, int gridY, String imagePath) {
        this(gridX, gridY, imagePath, null);
    }

    public Chair(int gridX, int gridY) {
        this(gridX, gridY, DEFAULT_IMAGE_PATH, null);
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