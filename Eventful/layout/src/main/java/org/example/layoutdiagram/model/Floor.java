package org.example.layoutdiagram.model;

import javafx.scene.paint.Color;

public class Floor extends Furniture {
    private static final Color DEFAULT_COLOR = Color.BLUE;

    public Floor(int gridX, int gridY) {
        super(gridX, gridY, DEFAULT_COLOR, false);
    }
} 