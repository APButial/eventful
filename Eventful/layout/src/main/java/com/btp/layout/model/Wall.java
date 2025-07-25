package com.btp.layout.model;

public class Wall extends Furniture {
    private static final String DEFAULT_IMAGE_PATH = "/com/btp/layout/images/wall.png";

    public Wall(int gridX, int gridY) {
        this(gridX, gridY, DEFAULT_IMAGE_PATH);
    }

    public Wall(int gridX, int gridY, String imagePath) {
        super(gridX, gridY, imagePath, false, FurnitureType.WALL, "W");
    }

    // Copy constructor
    private Wall(Wall other) {
        super(other);
    }

    @Override
    public Furniture createCopy() {
        return new Wall(this);
    }
} 