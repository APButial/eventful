package com.btp.layout.model;

public class Wall extends Furniture {
    private static final String IMAGE_PATH = "/com/btp/layout/images/wall.png";

    public Wall(int gridX, int gridY) {
        super(gridX, gridY, IMAGE_PATH, false);
    }
} 