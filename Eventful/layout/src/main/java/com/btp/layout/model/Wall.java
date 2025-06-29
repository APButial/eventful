package com.btp.layout.model;

import javafx.scene.paint.Color;

public class Wall extends Furniture {
    private static final Color DEFAULT_COLOR = Color.YELLOW;

    public Wall(int gridX, int gridY) {
        super(gridX, gridY, DEFAULT_COLOR, false);
    }
} 