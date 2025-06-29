package com.btp.layout.model;

import javafx.scene.paint.Color;

public class Chair extends Furniture {
    private static final Color DEFAULT_COLOR = Color.GREEN;

    public Chair(int gridX, int gridY) {
        super(gridX, gridY, DEFAULT_COLOR, false);
    }
} 