package com.btp.layout.model;

import javafx.scene.paint.Color;

public class Table extends Furniture {
    private static final Color DEFAULT_COLOR = Color.RED;

    public Table(int gridX, int gridY) {
        super(gridX, gridY, DEFAULT_COLOR, true);
    }
} 