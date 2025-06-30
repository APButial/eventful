package com.btp.layout.model;

public class Chair extends Furniture {
    private static final String IMAGE_PATH = "/com/btp/layout/images/chair.png";

    public Chair(int gridX, int gridY) {
        super(gridX, gridY, IMAGE_PATH, false);
    }
} 