package com.btp.layout.model;

public class Floor extends Furniture {
    private static final String IMAGE_PATH = "/com/btp/layout/images/floor.png";

    public Floor(int gridX, int gridY) {
        super(gridX, gridY, IMAGE_PATH, false);
    }
} 